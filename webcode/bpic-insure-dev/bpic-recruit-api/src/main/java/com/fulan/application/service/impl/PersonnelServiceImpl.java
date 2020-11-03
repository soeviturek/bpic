package com.fulan.application.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import com.bpic.common.constant.CommenConstant;
import com.bpic.common.core.domain.entity.SysLoginUser;
import com.bpic.common.core.domain.model.LoginUser;
import com.bpic.common.core.vo.DictVo;
import com.bpic.common.utils.StringUtils;
import com.bpic.common.utils.id.IdGenerator;
import com.bpic.common.utils.id.Idfactory;
import com.bpic.common.utils.json.JsonUtil;
import com.bpic.common.utils.page.PageInfo;
import com.bpic.common.utils.reponse.Response;
import com.bpic.system.domain.vo.TempCdeSalesVo;
import com.bpic.system.mapper.SysLoginUserMapper;
import com.bpic.system.mapper.SysUserMapper;
import com.bpic.system.service.ISysDictTypeService;
import com.fulan.api.flow.domain.FlowAction;


import com.fulan.api.personnel.domain.*;
import com.fulan.api.personnel.vo.*;


import com.fulan.application.mapper.*;
import com.fulan.application.service.PersonnelService;
import com.fulan.application.service.ShareInfoService;

import com.fulan.application.util.EmployeeUtil;



import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bpic.framework.web.service.TokenService;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

@Service("PersonnelServiceImpl")
public class PersonnelServiceImpl extends ServiceImpl<PersonnelMapper, Personnel> implements PersonnelService {

    @Value("${recruit.web.url:1}")
    private String recruitUrl;

	@Autowired
    PersonnelMapper personnelMapper;

	@Autowired
	InterviewActionMapper interviewActionMapper;

	@Autowired
	FamilyMemberMapper familyMemberMapper;

	@Autowired
	EducationalMapper educationalMapper;

	@Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    private TokenService tokenService;

	@Autowired
	IdGenerator idGenerator;



	@Autowired
	WorkExperienceMapper workExperienceMapper;

	@Autowired
	FlowActionMapper flowActionService;

	@Autowired
    EmployeeUtil employeeUtil;




	@Autowired
    ShareInfoService shareInfoService;

	@Autowired
    PresentRecordMapper presentRecordMapper;



	@Autowired
    private CheckMapper checkMapper;


	@Value("${erCoreFilePath:\\Doc\\Employee\\File\\Er\\}")
	private String path;

	//	签约后证件照回传
	@Value("${personnel.bareheaded.phote.task.url:1}")
	private String url1;

	//	签约后附件回传
	@Value("${personnel.attachment.task.url:1}")
	private String url2;

	private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*10);


	Logger logger= LoggerFactory.getLogger(PersonnelServiceImpl.class);

	private int getPages(int size, int total) {
		int pages = 0;
		if (size == 0) {
			return 0;
		}
		pages = total / size;
		if (total % size != 0) {
			pages++;
		}
		return pages;
	}




	public PersonnelManageInfoVo PersonnelCheck(String paperId) {
		return personnelMapper.checkPersonnelManageInfoById(Long.valueOf(paperId));
	}

	@Override
	@Transactional
	public Response<String> saveInfo(Personnel personnelExpVo){
		Personnel personnel = setPersonnel(personnelExpVo);
		Response<String> resp = new Response<String>();
		//判断当前流程中是否存在该增员人才
		Personnel searchPersonnel = new Personnel();
		//根据手机号和姓名验证是否已经提交
		//searchPersonnel.setName(personnelExpVo.getName());
		searchPersonnel.setCellphone(personnelExpVo.getCellphone());
		List<Personnel> personnelList = personnelMapper.getPersonnelByRecord(searchPersonnel);
		if(personnelList!=null&&personnelList.size()>0){
			resp.setCode(Response.ERROR);
			resp.setMsg("您已提交成功，报聘流程进行中，请敬待通知");
			return resp;
		}

		Long personnelId = idGenerator.generate();
		 personnel.setId(personnelId);
		//获取当前人才的状态为录入则执行操作否则不执行
		//String personnelStatus = personnelMapper.selectPersonnelStatusById(personnelId);
		Personnel personnelFir = personnelMapper.selectById(personnelExpVo.getId());
			if(personnelFir==null){
				 personnelFir = new Personnel();
			}
			 personnelFir.setId(personnelId);
			 personnelFir.setPersonnelStatus("1");
			 personnelFir.setCreateTime(new Date());
			 personnelFir.setCreateUser(personnelExpVo.getCreateUser());
			 /*personnelFir.setName(personnelExpVo.getName());
			 personnelFir.setCellphone(personnelExpVo.getCellphone());*/
			 personnelMapper.insertInfo(personnelFir);

		if("1".equals(personnelFir.getPersonnelStatus())){
			//根据职级设置初审或复审
			personnel.setPersonnelStatus(getPersonnelStatus(personnelExpVo));
			personnel.setUpdateTime(new Date());
			personnel.setUpdateUser(personnelId);

			InterviewAction interviewAction = new InterviewAction();
			interviewAction.setPersonnelId(personnelId);
			interviewAction.setId(idGenerator.generate());

			String updateStatus = getPersonnelStatus(personnelExpVo);
			interviewActionMapper.updateByPrimaryPersonnel(interviewAction);
			interviewAction.setFlowItemId(Long.valueOf(updateStatus));
			if("003".equals(personnel.getChannel())){
				EmployeeVo resultEmployee=null;
				Response<Map<String, List<Dictionary>>> dictResp = null; //todo dictionaryService.findByCodes("rank");
				List<Dictionary> dictList = dictResp.getData().get("rank");
				if(dictList!=null&&dictList.size()>0&&personnelFir.getCreateUser()!=null){
					//EmployeeVo employeeVo= customerService.queryEmployeeByPresentCode(personnelFir.getCreateUser());
                    TempCdeSalesVo employeeVo =  sysUserMapper.selectUserById(Long.valueOf(personnelFir.getCreateUser()));
                    resultEmployee= employeeUtil.getFirstTrialEmployee(employeeVo, personnel.getProtocolPosition(), dictList);
					 String superDuty = null;//EmployeeUtil.getCode(employeeVo.getDutyName());
					 if(resultEmployee!=null){
						 if("业务副总".equals(superDuty)&&(resultEmployee.getSuperPresentCode()==null||"".equals(resultEmployee.getSuperPresentCode()))){
								interviewAction.setFlowItemId(Long.valueOf(CommenConstant.ER_Flow_Selection));
								personnel.setPersonnelStatus(CommenConstant.ER_Flow_Selection);
							}
						 interviewAction.setProcessingName(resultEmployee.getEmployeeName());
						 interviewAction.setProcessingPerson(resultEmployee.getPresentCode());

//						 SmsEmailBusinessVO smsEmailBusinessVO = new SmsEmailBusinessVO();
//						 SmsBusinessVO smsVO = new SmsBusinessVO();
//						 Map<String, Object> parameter = new  HashMap<String, Object>();
//						 smsVO.setSourceId("recruit");
//						 smsVO.setPhones(resultEmployee.getMobilePhone());
//						 parameter.put("quasi_broker", personnel.getName());
//
//						 if(CommenConstant.ER_Flow_Selection.equals(personnel.getPersonnelStatus())){
//							 if(personnel.getConfirmPosition()!=null){
//								 parameter.put("ratified_rank", personnel.getConfirmPosition());
//							 }else{
//								 parameter.put("ratified_rank", personnel.getProtocolPosition());
//							 }
//							 smsVO.setBusinessId("recruit_pass_first_interview_retrial_oficer");
//							 parameter.put("telePhone", personnel.getCellphone());
//						 }else if(CommenConstant.ER_Flow_first_time.equals(personnel.getPersonnelStatus())){
//							 if(personnel.getConfirmPosition()!=null){
//								 parameter.put("default_rank", personnel.getConfirmPosition());
//							 }else{
//								 parameter.put("default_rank", personnel.getProtocolPosition());
//							 }
//							 smsVO.setBusinessId("recruit_invitation_to_interview");
//							 parameter.put("telePhone", personnel.getCellphone());
//						 }
//						 smsVO.setParameter(parameter);
//						 smsEmailBusinessVO.setSmsVO(smsVO);
//						 smsBusinessService.sendSmsEmail(smsEmailBusinessVO);
					 }
				}
			}

		/*	interviewAction.setProcessingPerson("953555301552887890");
			interviewAction.setProcessingName("zg");*/
			interviewActionMapper.insert(interviewAction);
			String url = "";
			String content = "";
            url= null;
			//Dictionary dictionary = dictionaryService.findByCode("JTER_URL1");
//			if(null !=dictionary){
//				url=dictionary.getValue();
//			}
//			String presentCode = "";
//			try {
//				presentCode = AESUtil.encode(interviewAction.getProcessingPerson(),"songlink1234");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			if (interviewAction.getFlowItemId()==2){
				//初审收到XXX的资料，需要您XX
				//https://erecruit-uat.juntianbroker.com/web/home/#page=fircheck&personnelId=1106037319938342912
//				url = url +"/web/home/#page=fircheck&agentCode="+presentCode+"&personnelId="+interviewAction.getPersonnelId();
				url = url +"/web/home/#page=fircheck&agentCode="+interviewAction.getProcessingPerson()+"&personnelId="+interviewAction.getPersonnelId();
				content = "收到"+personnelExpVo.getName()+"的资料，需要您初审";
			}
			if (interviewAction.getFlowItemId()==3){
				//复审收到XXX的资料，需要您XX
				//https://erecruit-uat.juntianbroker.com/web/home/#page=recheck&personnelId=1112969403193884672
//				url = url +"/web/home/#page=recheck&agentCode="+presentCode+"&personnelId="+interviewAction.getPersonnelId();
				url = url +"/web/home/#page=recheck&agentCode="+interviewAction.getProcessingPerson()+"&personnelId="+interviewAction.getPersonnelId();
				content = "收到"+personnelExpVo.getName()+"的资料，需要您复审";
			}

			//录入结束
			personnelMapper.updateByPrimaryKeySelective(personnel);
			resp.setCode(Response.SUCCESS);
			resp.setMsg("更新个人信息成功");
			return resp;
		}

		resp.setCode(Response.SUCCESS);
		resp.setMsg("请勿重复录入");
		return resp;
	}

	@Override
	@Transactional
	public Response<Personnel> add(Personnel personnelExpVo) {
        LoginUser loginUser = tokenService.getLoginUser();// 获取用户对象
		Personnel personnel = setPersonnel(personnelExpVo);
		Long personnelId = idGenerator.generate();
		Response<Personnel> resp = new Response<Personnel>();
		//判断当前流程中是否存在该增员人才
		/*if(personnelExpVo.getName()!=null&&personnelExpVo.getCellphone()!=null){
			Personnel searchPersonnel = new  Personnel();
			searchPersonnel.setName(personnelExpVo.getName());
			searchPersonnel.setCellphone(personnelExpVo.getCellphone());
			List<Personnel> personnelList = personnelMapper.getPersonnelByRecord(searchPersonnel);
			if(personnelList!=null&&personnelList.size()>0){
				resp.setCode(Response.ERROR);
				resp.setMsg("增员经纪人已录入增员流程中!");
				return resp;
			}
		}*/
		personnel.setCreateTime(new Date());
/*		personnel.setIsInsuranceCompany(getIsInsuranceCompany(personnelExpVo));*/
		personnel.setCreateUser(loginUser.getUser().getC_emp_cde());
//		personnel.setOrgId(account.getOrgId());
//		personnel.setOrgName(account.getOrgName());
//		personnel.setCompanyId(account.getCompanyId());
//		personnel.setTrialResult(account.getPostType());
		personnel.setId(personnelId);
		personnel.setPersonnelStatus(CommenConstant.ER_Flow_signup);


		personnelMapper.insertInfo(personnel);

		InterviewAction interviewAction = getInterviewAction(personnelId, Long.valueOf(loginUser.getUser().getC_emp_cde()));
		interviewAction.setFlowItemId(Long.valueOf(CommenConstant.ER_Flow_signup));
		interviewAction.setProcessingPerson(loginUser.getUser().getC_emp_cde());
		interviewAction.setProcessingName(loginUser.getUser().getC_emp_cnm());
		interviewActionMapper.insert(interviewAction);


		resp.setData(personnel);

		resp.setCode(Response.SUCCESS);
		resp.setMsg("添加个人信息成功");

		return resp;
	}




	/**
	 * 人才详情-查看个人信息
	 */
	@Override
	public Response<Map<String, Object>> getPersonnel(Long personnelId) {
		System.out.println(personnelId);
		Map<String, Object> map = new HashMap<>();
		Response<Map<String, Object>> resp = new Response<Map<String, Object>>(Response.SUCCESS, "人才详情-查找个人信息成功");
		Personnel personnel = personnelMapper.selectByPersonnelId(personnelId);
		String createName = "无";
		if(personnel.getCreateUser()!=null&&!"".equals(personnel.getCreateUser())){
		    //todo 获取上级人员姓名
//			EmployeeVo employeeVo = customerService.queryEmployeeByPresentCode(personnel.getCreateUser());
//			if(employeeVo!=null){
//				createName = employeeVo.getEmployeeName();
//			}
		}
		map.put("personnel", personnel);
		map.put("workExperiences", personnel.getWorkIntroduction());

		map.put("createName", createName);
		resp.setData(map);
		return resp;
	}
	/**
	 * 查看个人教育信息
	 */

	public Response<PersonnelEducation> getPersonnelEdu(Long personnelId) {
		Response<PersonnelEducation> resp = new Response<PersonnelEducation>(Response.SUCCESS, "人才详情-查找个人教育信息成功");
		PersonnelEducation personnelEducation = personnelMapper.selectEduByPersonnelId(personnelId);
		resp.setData(personnelEducation);
		return resp;
	}

	/**
	 * 人才详情-根据证件编号查找个人信息
	 */

	public Response<Personnel> getPersonnelByIdentityCode(String identityCode) {
		Response<Personnel> resp = new Response<Personnel>(Response.SUCCESS, "根据证件编号查找个人信息成功");
		Personnel personnel = personnelMapper.getPersonnelByIdentityCode(identityCode);
		resp.setData(personnel);
		return resp;
	}

	/**
	 * 代理人人才库 accountId 当前登录用户id
	 */

	public Response<List<Personnel>> getAgentPersonnel(String accountId) {
		Response<List<Personnel>> resp = new Response<List<Personnel>>(Response.SUCCESS, "查找代理人人才库信息成功");
		List<Personnel> personnel = personnelMapper.getAgentPersonnel(Long.valueOf(accountId));
		resp.setData(personnel);
		return resp;
	}




	@Override
	@Transactional
	public String checkResult(Personnel record) {
		Personnel personnel = personnelMapper.selectByPersonnelId(record.getId());
		personnel.setCheckResult(record.getCheckResult());
		personnel.setCheakOption(record.getCheakOption());
		int lg = personnelMapper.updateByPrimaryKeySelective(personnel);
     	if(1==lg){
			return record.getId().toString();
		}else{
			return null;
		}
	}



	public int  selectteamscalecountbyaccountId(List<Long> accountIds){


		return personnelMapper.selectteamscalecountbyaccountId(accountIds);
	}


	public PageInfo<PersonnelManageVo> listByPage(Page<PersonnelManageVo> page, SearchPersonnelVo searchPersonnelVo) {
		PageInfo<PersonnelManageVo> pageInfo = new PageInfo<PersonnelManageVo>();
		if(null !=searchPersonnelVo.getPersonnelStatus() && "" != searchPersonnelVo.getPersonnelStatus() && searchPersonnelVo.getPersonnelStatus().equals("6")){
			pageInfo.setRecords(personnelMapper.PersonnelCheckManageSearch(page,searchPersonnelVo) );
		}else{
			pageInfo.setRecords(personnelMapper.PersonnelManageSearch(page,searchPersonnelVo) );
		}
		int total = personnelMapper.PersonnelManageSearchCount(searchPersonnelVo);
		pageInfo.setPageNo(page.getCurrent());
		pageInfo.setPageSize(page.getSize());
		pageInfo.setPageTotal(getPages(searchPersonnelVo.getPageSize(), total));
		pageInfo.setPageRecords(page.getTotal());
		return pageInfo;
	}


	public PageInfo<PersonnelManageVo> listByOtherPage(Page<PersonnelManageVo> page, String keyWord, int pageNo,
                                                       int pageSize) {

		PageInfo<PersonnelManageVo> pageInfo = new PageInfo<PersonnelManageVo>();
		pageInfo.setRecords(personnelMapper.PersonnelManageOtherSearch(page, keyWord, pageNo, pageSize));
		int total = personnelMapper.personnelManageOtherSearchCount(keyWord);
		pageInfo.setPageNo(page.getCurrent());
		pageInfo.setPageSize(page.getSize());
		pageInfo.setPageTotal(getPages(pageSize, total));
		pageInfo.setPageRecords(page.getTotal());
		return pageInfo;
	}


	public PageInfo<PersonnelManageVo> listByOtherTwoPage(Page<PersonnelManageVo> page, String keyWord, int pageNo,
                                                          int pageSize) {
		PageInfo<PersonnelManageVo> pageInfo = new PageInfo<PersonnelManageVo>();
		pageInfo.setRecords(personnelMapper.PersonnelManageOtherTwoSearch(page, keyWord, pageNo, pageSize));
		int total = personnelMapper.personnelManageOtherTwoSearchCount(keyWord);
		pageInfo.setPageNo(page.getCurrent());
		pageInfo.setPageSize(page.getSize());
		pageInfo.setPageTotal(getPages(pageSize, total));
		pageInfo.setPageRecords(page.getTotal());
		return pageInfo;
	}



	@Override
	@Transactional
	public Response<String> insertSpecific(InterviewActionVo interviewActionVo) {
		Response<String> res=new Response<>(Response.SUCCESS, "操作成功");
		interviewActionVo.setId(Idfactory.generate());
		interviewActionVo.setStartTime(new Date());

		InterviewAction updataInterviewAction = new InterviewAction();
		updataInterviewAction.setPersonnelId(Long.valueOf(interviewActionVo.getPersonnelId()));
		updataInterviewAction.setProcessingPerson(interviewActionVo.getProcessingPerson());
		updataInterviewAction.setProcessingDesc(interviewActionVo.getProcessingDesc());
		updataInterviewAction.setEndTime(new Date());
//		修改培训通过后的状态
		updataInterviewAction.setProcessingStatus("1");
        LoginUser loginUser = tokenService.getLoginUser();
//		updataInterviewAction.setProcessingDesc(interviewActionVo.getProcessingDesc()==null?account.getAccountName()+"指定流程":interviewActionVo.getProcessingDesc());
//		更新当前流程，结束当前流程信息
		interviewActionMapper.updateByPrimaryPersonnel(updataInterviewAction);
//		插入指派流程的面试流程数据
		int num=interviewActionMapper.insertInterviewAction(interviewActionVo);
		if(num<=0){
			res.setCode(Response.ERROR);
			res.setMsg("操作失败！请重试");
		}else{
			personnelMapper.updatePersonnelStatus(interviewActionVo.getPersonnelId(), interviewActionVo.getFlowItemId());
//			if(interviewActionVo.getFlowItemId().equals("7")){
//				personnelMapper.updatePersonnelisSend(interviewActionVo.getPersonnelId());
//			}

		}
		return res;
	}


	/**
	 * @Author Mr.chen
	 * @Description
	 * //TODO 支持往前指派（初/复审）流程
	 * @Date
	 * @Param
	 * @return
	 **/
	@Transactional
	public Response<String> insertSpecificNew(InterviewActionVo interviewActionVo) {
		try {
			logger.info("insertSpecific1:"+ JsonUtil.getJSONString(interviewActionVo));
		} catch (Exception e) {
		}
		if(StringUtils.isEmpty(interviewActionVo.getPersonnelId())){
			logger.info("insertSpecific1:personnelId不能为空！");
			return new Response<>(Response.ERROR,Response.ERROR_MESSAGE);
		}
		Personnel personnel = personnelMapper.selectByPersonnelId(Long.valueOf(interviewActionVo.getPersonnelId()));
		if(personnel!=null && (personnel.getAgentCode()==null|| "".equals(personnel.getAgentCode()))){
			logger.info("insertSpecific1:"+interviewActionVo.getPersonnelId()+"当前人处于已完成状态！");
			return new Response<>(Response.ERROR,"当前人处于已完成状态！");
		}
		//1、判断当前是往前往后指派
		InterviewAction interviewAction = interviewActionMapper.getNowInterviewAction(Long.valueOf(interviewActionVo.getPersonnelId()));
		//当前流程大于目标指派流程
		if(interviewAction.getFlowItemId() < Long.valueOf(interviewActionVo.getFlowItemId())){
			logger.info("往后指派流程...");
			//1.1、流程往后指派
			return insertSpecific(interviewActionVo);
		}else{
			logger.info("往前指派流程...");
			//1.2、流程往前指派
			//1.2.1、更新流程信息（删除已经存在的流程信息，将最初的流程结束时间和审核结果置空，备注更新）
			InterviewAction interviewAction1 = new InterviewAction();
			interviewAction1.setPersonnelId(Long.valueOf(interviewActionVo.getPersonnelId()));
			//最初的流程
			InterviewAction interviewAction2 = interviewActionMapper.getFirstInterviewAction(interviewAction1);
			int num = interviewActionMapper.backInterviewAction(interviewAction2);
			int num1 = interviewActionMapper.updateInterviewAction(interviewAction2);
			//1.2.2、更新个人信息（审核结果置空，人员给当前流程修改,转发信息的状态修改）
			int num2 = personnelMapper.backFlow(interviewAction2);
			//1.2.3、删除工作经历处理为空的数据（非同业经历）
			int num3 = workExperienceMapper.backFlow(interviewAction2);

			logger.info("往前指派流程结束.");
			return new Response<>(Response.SUCCESS, "操作成功");
		}
	}



	public PersonnelFlowItemVo selectPersonner(String id) {
		PersonnelFlowItemVo personnelFlowItemVo=personnelMapper.selectPersonnerById(id);
		Long idl=null;
		if(null !=id && "" !=id){
			 idl=Long.valueOf(id);
		}
		List<FamilyMember>  familyMember=familyMemberMapper.selecPersonnelById(idl);
		List<WorkExperience>  workExperience= workExperienceMapper.selectByPersonnelId(idl);
		List<Educational>  educational= educationalMapper.selectByPersonnelId(idl);
		personnelFlowItemVo.setFamilyMember(familyMember);
		personnelFlowItemVo.setWorkExperience(workExperience);
		personnelFlowItemVo.setEducational(educational);
		FlowItem fl=personnelMapper.selectNowItem(id);
		InterviewAction interviewAction=personnelMapper.selectInterviewActionById(id);
		personnelFlowItemVo.setInterviewAction(interviewAction);
		if(null != fl){
			personnelFlowItemVo.setItem(fl.getFlowItemName());
			personnelFlowItemVo.setItemName(fl.getMoudleName());
		}

		List<FlowItem> flowItem=personnelMapper.selectFlowItem();
		List<FlowItemActionPersonnerVo> flowItemPersonner=personnelMapper.selectFlowPersonner(id);
		personnelFlowItemVo.setFlowItem(flowItem);
		personnelFlowItemVo.setFlowItemPersonner(flowItemPersonner);
		return personnelFlowItemVo;
	}




	public CheckPersonnelDataVo checkData(String id) {
		CheckPersonnelDataVo checkPersonnelDataVo=personnelMapper.checkData(id);
		return checkPersonnelDataVo;
	}

	private String getCard(String k, Personnel personnel) {
		return (("1".equals(k)| "2".equals(k))?personnel.getIdentityCode():"42".equals(k)?personnel.getChinaCiticBank():"");
	}



	@Transactional
	public void checkFail(String personnelId) {
		Personnel personnel = personnelMapper.selectByPersonnelId(Long.valueOf(personnelId));
		personnel.setAgentCode(null);
		personnelMapper.updateByPrimaryKey(personnel);
	}


	@Transactional
	public Response<String> checkDataResult(String id, String cheakPption, String checkResult, String accountName, String accountId) {
		Response<String> res=new Response<>(Response.SUCCESS, "操作成功");
		if(checkResult.equals("1")){
			Integer num=personnelMapper.checkDataResult(id,cheakPption,checkResult,null);
			InterviewActionVo interviewActionVo =new InterviewActionVo();
			interviewActionVo.setPersonnelId(id);
			interviewActionVo.setNowItem("7");
			interviewActionVo.setProcessingStatus(checkResult);
			interviewActionVo.setProcessingDesc(cheakPption);
			interviewActionVo.setProcessingName(accountName);
			interviewActionVo.setProcessingPerson(accountId);
			interviewActionVo.setEndTime(new Date());
			int inde=interviewActionMapper.updateSpecific(interviewActionVo);
			if(num<=0){
				res.setCode(Response.ERROR);
				res.setMsg("操作失败，请重试");
			}
		}
		if(checkResult.equals("2")){
			//不通过删除流程7，同时将人员信息置回步骤6（资料填写）
			interviewActionMapper.deleteByPersonnelID(id,"7");
			personnelMapper.updatePersonnelStatus(id, "6");
			//核查结果更新
			Integer num=personnelMapper.checkDataResult(id,cheakPption,null,null);
		}
		return res;
	}

	/**
	 * 新增代理人
	 * @param id
	 * @return
	 */
	@Transactional
	public Response<Object> addEmployeeNew(String id) {

		Response<Object> resp = new Response<Object>(Response.SUCCESS,"签约成功");
		try {
		//获取人员信息
		Personnel personnel = personnelMapper.selectByPersonnelId(Long.valueOf(id));
		//获取推荐人信息
		String createUser = null;
		if(personnel.getCreateUser()!=null){
			createUser = personnel.getCreateUser();
		}else{
			createUser = personnel.getSource();
		}
		if(createUser==null){
			return new Response<Object>(Response.ERROR,"签约失败");
		}
		EmployeeNew searchEmployeeNew = new  EmployeeNew();
		InterviewAction searchInterviewAction = new InterviewAction();
		searchInterviewAction.setPersonnelId(personnel.getId());
		//——!!内勤时无面试
		InterviewAction reInterviewAction = interviewActionMapper.getFirstInterviewAction(searchInterviewAction);
		searchEmployeeNew.setPresentCode(reInterviewAction.getProcessingPerson());
		//内勤招募——无面试流程设定初审面试人。
//		if( reInterviewAction != null ){
//			searchEmployeeNew.setPresentCode(reInterviewAction.getProcessingPerson());
//		}else{
//			内勤人员上级写死
//			searchEmployeeNew.setPresentCode("FY100");
//		}
//		searchEmployeeNew.setPresentCode(reInterviewAction.getProcessingPerson());
		List<HashMap<String, Object>>  employeeVoList = null ;//todo customerService.queryListNew(searchEmployeeNew);
		Map<String,Object> employeeVo = null;
		if(employeeVoList!=null&&employeeVoList.size()>0){
			employeeVo = employeeVoList.get(0);
		}
		//根据最终职级获取代理人上级信息，考虑到无上级的情况，此处做异常处理
		try {
			logger.info("-------------------【"+personnel.getName()+"】面试完毕，签约获取上级代理人信息-------------------------");
//			EmployeeVo searchEmployeeVo= customerService.queryEmployeeByPresentCode(personnel.getCreateUser());
//			EmployeeVo resultEmployee=null;
//			Response<Map<String, List<Dictionary>>> dictResp = dictionaryService.findByCodes("rank");
//			List<Dictionary> dictList = dictResp.getData().get("rank");
//			String position = personnel.getProtocolPosition();
//			if(!StringUtil.isEmpty(personnel.getConfirmPosition())){
//				position = personnel.getConfirmPosition();
//			}
//			resultEmployee= employeeUtil.getFirstTrialEmployee(searchEmployeeVo, position, dictList);
//			searchEmployeeNew.setPresentCode(resultEmployee.getPresentCode());
			searchEmployeeNew.setPresentCode(personnel.getCreateUser());
			employeeVoList = null; //todo customerService.queryListNew(searchEmployeeNew);
			employeeVo = employeeVoList.get(0);
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("-------------------【"+personnel.getName()+"】面试完毕，签约获取上级代理人信息失败，失败原因:"+e.getMessage());
		}
		//List<WorkExperience> workExperiences = workExperienceMapper.selectByPersonnelId(Long.valueOf(id));
		//教育信息
		List<Educational> listEdu = educationalMapper.selectByPersonnelId(personnel.getId());
		Educational educational = null;
		if(listEdu!=null&&listEdu.size()>0){
			educational = listEdu.get(0);
		}
		if(educational!=null){
			personnel.setEducation(EmployeeUtil.getEduCode(educational.getEducation()));
		}

		//紧急联系人
		List<FamilyMember> famList = familyMemberMapper.selecPersonnelById(personnel.getId());
		FamilyMember familyMember = null;
		if(famList!=null&&famList.size()>0){
			familyMember = famList.get(0);
		}

		//生成presentCode
		String preCode = null;
//		Response<Organization> organizationResp = organizationService.findByCode(personnel.getCompanyId());
//		if(organizationResp.getData()!=null&&organizationResp.getData().getCity()!=null&&organizationResp.getData().getProvince()!=null){
//			preCode = EmployeeNumberUtil.employNumber(organizationResp.getData().getProvince(),organizationResp.getData().getCity(), new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
//		}else{
//			return new Response<Object>(Response.ERROR,"签约失败");
//		}
			//todo 工号
//		Response<OrganizationChart> organizationChartResp = organizationChartService.getOrganizationChart(personnel.getCompanyId());
//		if(organizationChartResp.getData()!=null&&organizationChartResp.getData().getCityName()!=null&&organizationChartResp.getData().getProvinceName()!=null){
//			preCode = EmployeeNumberUtil.employNumber(organizationChartResp.getData().getProvinceName(),organizationChartResp.getData().getCityName(), new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
//		}else{
//			return new Response<Object>(Response.ERROR,"签约失败");
//		}

		String subCodeLast = presentRecordMapper.selectLastRecord(preCode);
		if(subCodeLast==null){
			subCodeLast ="2000";
		}

		String subCode = "";
		PresentRecord insertPresentRecord = new PresentRecord();
		Boolean isCheck = true;
		try {
			while (isCheck){
				logger.info("工号生成:"+preCode+subCode);
				subCode = null;//todo EmployeeNumberUtil.getSubCodeStr(1+Integer.valueOf(subCodeLast));
				insertPresentRecord.setId(idGenerator.generate());
				insertPresentRecord.setPreCode(preCode);
				insertPresentRecord.setSubCode(subCode);
				insertPresentRecord.setType("1");
				insertPresentRecord.setCreateTime(new Date());
				EmployeeNew employeeNew = new EmployeeNew();
				employeeNew.setPresentCode(preCode+subCode);
//				if(customerService.queryListNew(employeeNew).size() <= 0){
//					logger.info("工号:"+preCode+subCode+"校验完成！");
//					isCheck = false;
//				}
                subCodeLast = String.valueOf((Integer.valueOf(subCodeLast)+1));

			}
		}catch (Exception e){
			logger.error("生成工号异常！",e);
			throw new RuntimeException("生成工号异常!");
		}

		//insertPresentRecord.setFullCode(preCode+subCode);
		presentRecordMapper.insert(insertPresentRecord);

		Personnel upPersonnel = new Personnel();
		upPersonnel.setAgentCode(preCode+subCode);
		upPersonnel.setId(personnel.getId());
		personnelMapper.updateById(upPersonnel);

		personnel.setAgentCode(preCode+subCode);

//		EmployeeNewVo employeeNewVo = new EmployeeNewVo();
//		employeeNewVo.setEmployeeNew(saveEmployeeNew(personnel, employeeVo,familyMember));
//		employeeNewVo.setEmployeeNewListVo(saveEmployeeNewListVo(personnel,employeeVo));
//		System.out.println(JSONObject.toJSONString(employeeNewVo));
//		logger.info("-------------------"+"创建代理人传递参数"+employeeNewVo);
//		resp = customerService.addEmployeeNew(employeeNewVo);
		resp.setData(preCode+subCode);
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("-------------------"+"创建代理人异常",e);
			return new Response<Object>(Response.ERROR,"签约失败");
		}
		return resp;
	}


	public List<ExportPersonnelVo> selectExportExcel(SearchPersonnelVo searchPersonnelVo) {
//		logger.info(new Date()+"=======selectExportExcel方法开始=======");
//		List<ExportPersonnelVo> selectExportExcel = personnelMapper.selectExportExcel(searchPersonnelVo);
////		线程优化数据的获取。
//		Map map = new HashMap();
//		map.put("dictionaryService",dictionaryService);
//		map.put("customerService",customerService);
//		map.put("attachmentService",attachmentService);
//		map.put("searchPersonnelVo",searchPersonnelVo);
//		map.put("familyMemberMapper",familyMemberMapper);
//		map.put("workExperienceMapper",workExperienceMapper);
////		map.put("organizationService",organizationService);
//		map.put("organizationChartService",organizationChartService);
//		logger.info(new Date()+"=======线程开始=======");
//		Future<List<ExportPersonnelVo>> futures = executorService.submit(new ExportPersonnelHandleTask(selectExportExcel,executorService,map));
//		List<ExportPersonnelVo> ExportExcel=new ArrayList<>();
//		try {
//			ExportExcel = futures.get();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}
//		logger.info(new Date()+"=======线程结束=======");
//		for (ExportPersonnelVo exportPersonnelVo : selectExportExcel) {
//			//查询身份证
//			Dictionary dictionary = null;
//			if(null!=exportPersonnelVo.getIdentityType() && ""!=exportPersonnelVo.getIdentityType()){
//				dictionary=dictionaryService.findByCode(exportPersonnelVo.getIdentityType());
//			}
//			if(null !=dictionary){
//				exportPersonnelVo.setIdentityType(dictionary.getCnName());
//			}
//			//查询推荐人
//			if(null !=exportPersonnelVo.getCreatUser()){
//				EmployeeVo  employeeVo= customerService.queryEmployeeByPresentCode(exportPersonnelVo.getCreatUser());
//				if(null != employeeVo){
//					exportPersonnelVo.setTpersonnel(employeeVo.getEmployeeName());
//				}
//			}
//			//查询合同编号
//			Attachment attachmentContractNum =attachmentService.selectContractNum(exportPersonnelVo.getId());
//			if(null !=attachmentContractNum){
//				exportPersonnelVo.setContractNum(attachmentContractNum.getDescription());
//			}
//			//员工编号
//			exportPersonnelVo.setEmployeeNum(exportPersonnelVo.getEmployeeNum());
//			if(searchPersonnelVo.getPersonnelSign().equals("1")){
//				exportPersonnelVo.setNowItem("已完成");
//			}
//			Long idl=exportPersonnelVo.getId();
//			List<FamilyMember>  familyMember=familyMemberMapper.selecPersonnelById(idl);
//			if(null != familyMember && familyMember.size()==1){
//				exportPersonnelVo.setEmergencyContactOne(familyMember.get(0).getName());
//				exportPersonnelVo.setRelationOne(familyMember.get(0).getRelationship());
//				exportPersonnelVo.setRelationPhoneOne(familyMember.get(0).getTelephone());
//				exportPersonnelVo.setEmergencyContactTwo("无");
//				exportPersonnelVo.setRelationTwo("无");
//				exportPersonnelVo.setRelationPhoneTwo("无");
//			}
//			if(null != familyMember && familyMember.size()==2){
//				exportPersonnelVo.setEmergencyContactOne(familyMember.get(0).getName());
//				exportPersonnelVo.setRelationOne(familyMember.get(0).getRelationship());
//				exportPersonnelVo.setRelationPhoneOne(familyMember.get(0).getTelephone());
//				exportPersonnelVo.setEmergencyContactTwo(familyMember.get(1).getName());
//				exportPersonnelVo.setRelationTwo(familyMember.get(1).getRelationship());
//				exportPersonnelVo.setRelationPhoneTwo(familyMember.get(1).getTelephone());
//			}
//			List<WorkExperience>  workExperience= workExperienceMapper.selectByPersonnelId(idl);
//			if(null != workExperience && workExperience.size()==0){
//				exportPersonnelVo.setWorkStartTimeOne("无");
//				exportPersonnelVo.setWorkEndTimeOne("无");
//				exportPersonnelVo.setCompanyOne("无");
//				exportPersonnelVo.setDepartmentOne("无");
//				exportPersonnelVo.setPositionOne("无");
//				exportPersonnelVo.setWorkStartTimeTwo("无");
//				exportPersonnelVo.setWorkEndTimeTwo("无");
//				exportPersonnelVo.setCompanyTwo("无");
//				exportPersonnelVo.setDepartmentTwo("无");
//				exportPersonnelVo.setPositionTwo("无");
//				exportPersonnelVo.setWorkStartTimeThree("无");
//				exportPersonnelVo.setWorkEndTimeThree("无");
//				exportPersonnelVo.setCompanyThree("无");
//				exportPersonnelVo.setDepartmentThree("无");
//				exportPersonnelVo.setPositionThree("无");
//			}
//			if(null != workExperience && workExperience.size()==1){
//				exportPersonnelVo.setWorkStartTimeOne(DateUtil.toDay(workExperience.get(0).getStartTime()));
//				exportPersonnelVo.setWorkEndTimeOne(DateUtil.toDay(workExperience.get(0).getEndTime()));
//				exportPersonnelVo.setCompanyOne(workExperience.get(0).getCompany());
//				exportPersonnelVo.setDepartmentOne("无");
//				exportPersonnelVo.setPositionOne(workExperience.get(0).getPost());
//				exportPersonnelVo.setWorkStartTimeTwo("无");
//				exportPersonnelVo.setWorkEndTimeTwo("无");
//				exportPersonnelVo.setCompanyTwo("无");
//				exportPersonnelVo.setDepartmentTwo("无");
//				exportPersonnelVo.setPositionTwo("无");
//				exportPersonnelVo.setWorkStartTimeThree("无");
//				exportPersonnelVo.setWorkEndTimeThree("无");
//				exportPersonnelVo.setCompanyThree("无");
//				exportPersonnelVo.setDepartmentThree("无");
//				exportPersonnelVo.setPositionThree("无");
//			}
//			if( null != workExperience && workExperience.size()==2){
//				exportPersonnelVo.setWorkStartTimeOne(DateUtil.toDay(workExperience.get(0).getStartTime()));
//				exportPersonnelVo.setWorkEndTimeOne(DateUtil.toDay(workExperience.get(0).getEndTime()));
//				exportPersonnelVo.setCompanyOne(workExperience.get(0).getCompany());
//				exportPersonnelVo.setDepartmentOne("无");
//				exportPersonnelVo.setPositionOne(workExperience.get(0).getPost());
//				exportPersonnelVo.setWorkStartTimeTwo(DateUtil.toDay(workExperience.get(1).getStartTime()));
//				exportPersonnelVo.setWorkEndTimeTwo(DateUtil.toDay(workExperience.get(1).getEndTime()));
//				exportPersonnelVo.setCompanyTwo(workExperience.get(1).getCompany());
//				exportPersonnelVo.setDepartmentTwo("无");
//				exportPersonnelVo.setPositionTwo(workExperience.get(1).getPost());
//				exportPersonnelVo.setWorkStartTimeThree("无");
//				exportPersonnelVo.setWorkEndTimeThree("无");
//				exportPersonnelVo.setCompanyThree("无");
//				exportPersonnelVo.setDepartmentThree("无");
//				exportPersonnelVo.setPositionThree("无");
//			}
//			if(null != workExperience &&  workExperience.size()==3){
//				exportPersonnelVo.setWorkStartTimeOne(DateUtil.toDay(workExperience.get(0).getStartTime()));
//				exportPersonnelVo.setWorkEndTimeOne(DateUtil.toDay(workExperience.get(0).getEndTime()));
//				exportPersonnelVo.setCompanyOne(workExperience.get(0).getCompany());
//				exportPersonnelVo.setDepartmentOne("无");
//				exportPersonnelVo.setPositionOne(workExperience.get(0).getPost());
//
//				exportPersonnelVo.setWorkStartTimeTwo(DateUtil.toDay(workExperience.get(1).getStartTime()));
//				exportPersonnelVo.setWorkEndTimeTwo(DateUtil.toDay(workExperience.get(1).getEndTime()));
//				exportPersonnelVo.setCompanyTwo(workExperience.get(1).getCompany());
//				exportPersonnelVo.setDepartmentTwo("无");
//				exportPersonnelVo.setPositionTwo(workExperience.get(1).getPost());
//
//				exportPersonnelVo.setWorkStartTimeThree(DateUtil.toDay(workExperience.get(2).getStartTime()));
//				exportPersonnelVo.setWorkEndTimeThree(DateUtil.toDay(workExperience.get(2).getEndTime()));
//				exportPersonnelVo.setCompanyThree(workExperience.get(2).getCompany());
//				exportPersonnelVo.setDepartmentThree("无");
//				exportPersonnelVo.setPositionThree(workExperience.get(2).getPost());
//			}
//			List<Integer> list=new ArrayList<>();
//			list.add(48);
//			list.add(49);
//			list.add(CommenConstant.ER_IDEN_PHOTO_POSITIVE);
//			list.add(CommenConstant.ER_IDEN_PHOTO_REVERSE);
//			list.add(CommenConstant.ER_BANK);
//			list.add(CommenConstant.EL_Honor_Qualification);
//			list.add(CommenConstant.ER_EDUCATION_PROVE);
//			List<Attachment> findImgListByTableAndHostId = attachmentService.findImgListByTableAndHostId(list,idl);
//			String pub=dictionaryService.findByCode("backPath_webManage").getValue()+"/"+CommenConstant.mediaPlay;
//			for (Attachment attachment : findImgListByTableAndHostId) {
//				if(null != attachment){
//					if(attachment.getCategory().equals("48")){
//						exportPersonnelVo.setSignatureImg(pub+attachment.getUrl());
//					}else if(attachment.getCategory().equals("49")){
//						exportPersonnelVo.setBareheadedPhoto(pub+attachment.getUrl());
//					}else if(attachment.getCategory().equals("1")){
//						exportPersonnelVo.setFrontIdentification(pub+attachment.getUrl());
//					}else if(attachment.getCategory().equals("2")){
//						exportPersonnelVo.setBackIdentification(pub+attachment.getUrl());
//					}else if(attachment.getCategory().equals("42")){
//						exportPersonnelVo.setDebitCard(pub+attachment.getUrl());
//					}else if(attachment.getCategory().equals("43")){
//						exportPersonnelVo.setMortImg(pub+attachment.getUrl());
//					}else if(attachment.getCategory().equals("4")){
//						exportPersonnelVo.setStudyImg(pub+attachment.getUrl());
//					}
//				}
//			}
//			List<Integer> list1 = Arrays.asList(144,145,146,147);
//			List<Attachment>  attachment= attachmentService.findImgListByTableAndHostId(list1,idl);
//			for (Attachment attachment1 : attachment) {
//				if(null != attachment1){
//					if(attachment1.getCategory().equals("144")){
//						exportPersonnelVo.setIllegalFundRaisingInsurance(pub+attachment1.getUrl());
//					}else if(attachment1.getCategory().equals("145")){
//						exportPersonnelVo.setMortmoralGuidanceImg(pub+attachment1.getUrl());
//					}else if(attachment1.getCategory().equals("146")){
//						exportPersonnelVo.setPersonnelCommitment(pub+attachment1.getUrl());
//					}else if(attachment1.getCategory().equals("147")){
//						exportPersonnelVo.setMiddlemanDeal(pub+attachment1.getUrl());
//					}
//				}
//			}
//			if(exportPersonnelVo.getBusinessUnit() != null){
//				//根据营业部id获取上级事业部
//				Response<Organization> response = organizationService.findBusinessUnitByCompanyId(exportPersonnelVo.getBusinessUnit());
//				if("1".equals(response.getCode())){
//					//将事业部信息暂存在CompanyId传给前端
//					if(response.getData() != null){
//						Organization organization = response.getData();
//						exportPersonnelVo.setBusinessUnit(organization.getShortName());
//					}else{
//						exportPersonnelVo.setBusinessUnit("");
//					}
//				}
//			}
//			ExportExcel.add(exportPersonnelVo);
//		}

		logger.info(new Date()+"=======selectExportExcel方法结束=======");
		return null;//ExportExcel;
	}

	@Override
	@Transactional
	public Response<String> updatePersonnerStaus(String id, String personnelStatus) {
		Response<String> res=new Response<>(Response.SUCCESS, "操作成功");
		Integer num=personnelMapper.updatePersonnelStatus(id,personnelStatus);
		if(num<=0){
			res.setCode(Response.ERROR);
			res.setMsg("操作失败，请重试");
		}
		return res;

	}

	@Override
	@Transactional
	public Response<String> updateSpecific(InterviewActionVo interviewActionVo) {
	Response<String> res=new Response<>(Response.SUCCESS, "操作成功");
//		int num=interviewActionMapper.updateSpecific(interviewActionVo);
//		if(num<=0){
//			res.setCode(Response.ERROR);
//			res.setMsg("操作失败！请重试");
//			return res;
//		}else{
//			Personnel personnel = personnelMapper.selectByPersonnelId(Long.valueOf(interviewActionVo.getPersonnelId()));
//			//构建推送对象——面试通过进行下一步面试推送
//			PushVo pushVo = new PushVo();
//			//https://erecruit-uat.juntianbroker.com/web/home/#page=recheck&personnelId=1112969403193884672
//			String url = "";
//			String  content = "";
//			Dictionary dictionary = dictionaryService.findByCode("JTER_URL1");
//			if(null !=dictionary){
//				url=dictionary.getValue();
//			}
////			String presentCode = "";
////			try {
////				presentCode = AESUtil.encode(interviewActionVo.getProcessingPerson(),"songlink1234");
////			} catch (Exception e) {
////				e.printStackTrace();
////			}
//			if(interviewActionVo.getNowItem().equals("2")){
////				url = url +"/web/home/#page=fircheck&agentCode="+presentCode+"&personnelId="+personnel.getId();
//				url = url +"/web/home/#page=fircheck&agentCode="+interviewActionVo.getProcessingPerson()+"&personnelId="+personnel.getId();
//				content = "收到"+personnel.getName()+"的资料，需要您初审";
//			}
//			if(interviewActionVo.getNowItem().equals("3")){
////				url = url+"/web/home/#page=recheck&agentCode="+presentCode+"&personnelId="+personnel.getId();
//				url = url+"/web/home/#page=recheck&agentCode="+interviewActionVo.getProcessingPerson()+"&personnelId="+personnel.getId();
//				content = "收到"+personnel.getName()+"的资料，需要您复审。";
//			}
//			pushVo.setUrl(url);
//			pushVo.setJumpMode(2);
//			pushVo.setTitle(content);
//			pushVo.setContent(content);
//			pushVo.setType("4");
//			//推送模块的选择
//			Dictionary dictionary1 = dictionaryService.findByCode("JtPushModelType");
//			if(null !=dictionary1){
//				pushVo.setType(dictionary1.getValue());
//			}
//			ArrayList pushList = new ArrayList();
//			pushList.add(interviewActionVo.getProcessingPerson());
//			pushVo.setEmployeeNo(pushList);
////			pushService.signPush(pushVo);
//
////			四合一推送
//			VcardMessageVo vcardMessageVo = new VcardMessageVo();
//			vcardMessageVo.setMessageObjectType("1");
//			vcardMessageVo.setMessageType("2");
//			vcardMessageVo.setMessageUrl(url);
//			List<com.fulan.api.personnal.vo.AgentVo> list1 = new ArrayList<>();
//			com.fulan.api.personnal.vo.AgentVo agentVo = new com.fulan.api.personnal.vo.AgentVo();
//			agentVo.setAgentCode(interviewActionVo.getProcessingPerson());
//			agentVo.setAgentName(interviewActionVo.getProcessingName());
//			list1.add(agentVo);
//			vcardMessageVo.setMessageAgentCode(list1);
//			vcardMessageVo.setMessageName(content);
//			vcardMessageVo.setMessageContent(content);
//            vcardMessageVo.setMessageIsNotice(true);
//			vcardMessageVo.setSource("ER");
//			try {
//				logger.info("入参"+ JsonUtil.getJSONString(vcardMessageVo));
//				Response<Object> response = vcardMessageService.addVcardMessage(vcardMessageVo);
//				logger.info("出参"+ JsonUtil.getJSONString(response));
//			} catch (Exception e) {
//				logger.error(""+e);
//				e.printStackTrace();
//			}
//		}
////		personnelMapper.updatePersonnelById(interviewActionVo);
		return res;
	}



	@Transactional
	public Response<String> updateBatchPersonnelisSend(String ids) {
		Response<String> res=new Response<>(Response.SUCCESS, "批量转发成功");
		String[] split = ids.split(",");
		int inde=personnelMapper.updateBatchPersonnelisSend(split);
		if(inde<split.length){
			res.setCode(Response.ERROR);
			res.setMsg("转发失败请重试！");
		}
		return res;
	}




	@Transactional
	public Response<String> submitPersonnel(Personnel personnel) {

		Personnel rePersonnel = personnelMapper.selectById(personnel.getId());
//		关闭功能不允许提交个人详情资料
		if(rePersonnel.getIsClose()!=null && rePersonnel.getIsClose()==1){
			return new Response<>(Response.ERROR,"已关闭!");
		}

		personnelMapper.updateById(personnel);
		if(rePersonnel.getPersonnelStatus()!="7"){
			//更新流程
			InterviewAction updateInterviewAction = new InterviewAction();
			updateInterviewAction.setPersonnelId(personnel.getId());
			interviewActionMapper.updateByPrimaryPersonnel(updateInterviewAction);
			//插入流程
			InterviewAction interviewAction = new InterviewAction();
			interviewAction.setId(idGenerator.generate());
			interviewAction.setFlowItemId(Long.valueOf(7));
			interviewAction.setPersonnelId(personnel.getId());
			interviewAction.setStartTime(new Date());
			interviewActionMapper.insert(interviewAction);
		}
		String superCode = null;
		if(personnel.getCreateUser()!=null){
			superCode = personnel.getCreateUser();
		}else{
			superCode = personnel.getSource();
		}

		return new Response<String>(Response.SUCCESS, "提交成功");
	}



	@Override
	@Transactional
	public Response<String> updateInterviewActionByIdCode(String id, String nowItem,String status) {
		Response<String> res=new Response<>(Response.SUCCESS, "批量转发成功");
		//Date endTime = new Date();
		int num=interviewActionMapper.updateInterviewActionByIdCode(id,nowItem,status,null);
		return res;
	}


	private InterviewAction getInterviewAction(Long personnelId, Long createUser){
		InterviewAction interviewAction = new InterviewAction();
		interviewAction.setPersonnelId(personnelId);
		interviewAction.setId(idGenerator.generate());
		interviewAction.setCreateUser(createUser);

		return interviewAction;
	}

	private Personnel setPersonnel(Personnel personnelExpVo){
		Personnel personnel = new Personnel();

		personnel.setId(personnelExpVo.getId());
		personnel.setFamilyProvince(personnelExpVo.getFamilyProvince());
		personnel.setFamilyCity(personnelExpVo.getFamilyCity());
		personnel.setWechat(personnelExpVo.getWechat());
		personnel.setContactQQ(personnelExpVo.getContactQQ());
		personnel.setOriginalCompany(personnelExpVo.getOriginalCompany());
		personnel.setCellphone(personnelExpVo.getCellphone());
		personnel.setSex(personnelExpVo.getSex());
		personnel.setChannel(personnelExpVo.getChannel());
		personnel.setEmail(personnelExpVo.getEmail());
		personnel.setName(personnelExpVo.getName());
		personnel.setPersonnelStatus(personnelExpVo.getPersonnelStatus());
		personnel.setProtocolPosition(personnelExpVo.getProtocolPosition());
		personnel.setWorkIntroduction(personnelExpVo.getWorkIntroduction());
		personnel.setTotalPortfolio(personnelExpVo.getTotalPortfolio());
		personnel.setMaxCoacher(personnelExpVo.getMaxCoacher());

		//如果为复审则设置核定职级
		if ("3".equals(getPersonnelStatus(personnelExpVo))) {
			personnel.setConfirmPosition(personnelExpVo.getProtocolPosition());
		}
		return personnel;
	}
	/*private WorkExperience setWorkExperience(PersonnelExpVo personnelExpVo){
		WorkExperience workExperience = new WorkExperience();

		workExperience.setPost(personnelExpVo.getPost());
		workExperience.setCompany(personnelExpVo.getCompany());
		workExperience.setWorkIntroduction(personnelExpVo.getWorkIntroduction());

		return workExperience;
	}*/
	//根据职级设置初审或复审
	private String getPersonnelStatus(Personnel personnelExpVo){

		if(//"业务副总".equals(personnelExpVo.getProtocolPosition()) ||
				"高级行销总监".equals(personnelExpVo.getProtocolPosition())||
				"行销副总".equals(personnelExpVo.getProtocolPosition()))
		{
			return CommenConstant.ER_Flow_Selection;
		}

		return CommenConstant.ER_Flow_first_time;
	}
	//判断是否有工作经验
	/*private int getIsInsuranceCompany(Personnel personnelExpVo){

		if(StringUtil.isEmpty(personnelExpVo.getOriginalCompany())
				&& StringUtil.isEmpty(personnelExpVo.getWorkIntroduction())
				&& StringUtil.isEmpty(personnelExpVo.getPost())){
			return 2;
		}

		return 1;
	}*/

	/**
	 * 获取设置过参数的EmployeeNewListVo
	 * @return EmployeeNewListVo
	 */
//	private EmployeeNewListVo saveEmployeeNewListVo(Personnel personnel, Map<String,Object> employeeVo){

//		EmployeeNewListVo employeeNewListVo = new EmployeeNewListVo();
//
//		//目前要传的集合
//		employeeNewListVo.setDutyHistoryModel(addDutyHistoryModelList(personnel,employeeVo));
//		employeeNewListVo.setEducationHistoryModel(addEducationHistoryModellList(personnel));
//		employeeNewListVo.setResumeModel(addResumeModelList(personnel));
//		employeeNewListVo.setCertificateModel(addCertificateModelList(personnel));
//
//		//暂时不用传的集合
//	/*
//
//		employeeNewListVo.setTrainingHistoryModel(addTrainingHistoryModelList());
//
//		employeeNewListVo.setCertificateDetailFileModel(addCertificateDetailFileModelList());
//		employeeNewListVo.setEducationDetailFileModel(addEducationDetailFileModelList());
//		employeeNewListVo.setResumeDetailFileModel(addResumeDetailFileModelList());
//		employeeNewListVo.setTrainingDetailFileModel(addTrainingDetailFileModelList());*/
//
//		return employeeNewListVo;
//	}
//
//	/**
//	 * 获取设置过参数的EmployeeNew
//	 * @param personnel
//	 * @param employeeVo
//	 * @return EmployeeNew
//	 */
//	public EmployeeNew saveEmployeeNew(Personnel personnel, Map<String,Object> employeeVo, FamilyMember familyMember){
//		EmployeeNew employeeNew = new EmployeeNew();
//		//出生日期
//	//	employeeNew.setBirthday(FomartTimeUtil.ymdUtil(personnel.getBirthday()));
//		//分支机构ID
//		employeeNew.setBranchID(personnel.getOrgId());
//		//证件代码
//		employeeNew.setCertificateCode(personnel.getIdentityCode());
//		//证件类型
//		employeeNew.setCertificateType(EmployeeUtil.getIdCardCode(personnel.getIdentityType()));
//		//签约开始时间
//		//employeeNew.setContractBeginDate(FomartTimeUtil.ymdUtil(new Date()));
//		//签约结束时间
//		employeeNew.setContractEndDate(0);
//		//创建日期
//		//employeeNew.setCreatedDate(FomartTimeUtil.ymdhmsUtil(new Date()));
//		//创档人ID
//		employeeNew.setCreator(personnel.getCreateUser());
//		//目前地址
//		employeeNew.setCurrentAddress(personnel.getFamilyAddrDetail());
//		//当前电话
//		employeeNew.setCurrentPhone(personnel.getCellphone());
//		//目前所在地邮政编码
//		employeeNew.setCurrentZip(personnel.getPostcode());
//		//部门ID
//		employeeNew.setDepartmentID(String.valueOf(employeeVo.get("DepartmentID")));
//		//学历类型(0：小学1：初中2：高中3：专科4：本科5：硕士6：博士7：博士后 254：其他)
//		if(personnel.getEducation()!=null){
//			employeeNew.setEducationType(Byte.valueOf(personnel.getEducation()));
//		}
//		//邮箱
//		employeeNew.setEmail(personnel.getEmail());
//		//员工名称
//		employeeNew.setEmployeeName(personnel.getName());
//		//家庭地址
//		employeeNew.setFamilyAddress(personnel.getDomicilePlace());
//		//家庭电话
//		employeeNew.setFamilyPhone(personnel.getTelephone());
//		//介绍人Id
//		Map<String,Object> jsEmployVo = new HashMap<String,Object>();
//		if(personnel.getCreateUser()!=null&&!"".equals(personnel.getCreateUser())){
//			EmployeeNew searchEmployeeNew = new  EmployeeNew();
//			searchEmployeeNew.setPresentCode(personnel.getCreateUser());
//			List<HashMap<String, Object>>  employeeVoList = customerService.queryListNew(searchEmployeeNew);
//
//			if(employeeVoList!=null&&employeeVoList.size()>0){
//				jsEmployVo = employeeVoList.get(0);
//			}
//		}
//		employeeNew.setIntroducerID(String.valueOf(jsEmployVo.get("EmployeeID")));
//		//是否可用
//		employeeNew.setIsDeleted(0);
//		//最後修改日期(日期格式-YYYYMMDDHHMMSS）
//		employeeNew.setLastUpdateDate(FomartTimeUtil.ymdhmsUtil(new Date()));
//		//婚姻状况（0、未婚；1、已婚）
//		//employeeNew.setMarried((byte)0);
//		//备注
//		//employeeNew.setMemo(null);
//		//更新档人ID
//		//employeeNew.setMender(null);
//		//手机号码
//		employeeNew.setMobilePhone(personnel.getCellphone());
//		//民族ID
//		Dictionary dict = dictionaryService.findByCode(personnel.getNation());
//		if(dict!=null){
//			employeeNew.setNationID(Integer.valueOf(dict.getValue()));
//		}
//		//籍贯
//		//employeeNew.setNative();
//		//入司日期
//		employeeNew.setOnboardDate(FomartTimeUtil.ymdUtil(new Date()));
//		//户籍地址
//		employeeNew.setParentFamilyAddress(personnel.getDomicilePlace());
//		//相片地址
//		//employeeNew.setPhotoPath("");
//		//先前修改时间
//		//employeeNew.setPrecedUpateDate(0L);
//		//员工码
//		employeeNew.setPresentCode(personnel.getAgentCode());
//		//性别
//		if("M".equals(personnel.getSex())){
//			employeeNew.setSex(0);
//		}else{
//			employeeNew.setSex(1);
//		}
//
//		//紧急联络人地址
//		// employeeNew.setSOSAddress(null);
//		if(familyMember!=null){
//			//紧急联络人姓名
//			employeeNew.setSOSName(familyMember.getName());
//			//紧急联络人电话
//			employeeNew.setSOSPhone(familyMember.getTelephone());
//		}
//		//员工类型（0、内勤；1、外勤）
//		employeeNew.setType((byte)1);
//		//验证状态（0、解锁状态；1、上锁状态）
//		employeeNew.setValidate(0);
//		//验证日期
//		//employeeNew.setValidateDate(0);
//		//体重
//		//employeeNew.setWeight(0);
//		//合同号
//		List<Attachment> list = attachmentService.findbyparms(147, personnel.getId());
//		if(list!=null&&list.size()>0){
//			employeeNew.setContractNo(list.get(0).getDescription());
//		}
//		return null;
//	}

	/**
	 * 职务变迁信息集合
	 * @return List<DutyHistoryModel>
	 */
	//private List<DutyHistoryModel> addDutyHistoryModelList(Personnel personnel, Map<String,Object> employeeVo){

//		List<DutyHistoryModel> dutyHistoryModels = new ArrayList<>();
//		DutyHistoryModel dutyHistoryModel = new DutyHistoryModel();
//
//
//		dutyHistoryModel.setBranchID((String) employeeVo.get("BranchID"));//分支机构ID
//		dutyHistoryModel.setDepartmentID((String) employeeVo.get("DepartmentID"));//部门ID
////		dutyHistoryModel.setDutyHistoryID(DutyHistoryID);//职务变迁ID
////		dutyHistoryModel.setEmployeeID(EmployeeID);//员工代码
////		dutyHistoryModel.setEndDate(EndDate);//结束时间
//		dutyHistoryModel.setHireDate(Integer.valueOf(FomartTimeUtil.ymdUtil(new Date()).toString()));//到职时间
//		String post = personnel.getConfirmPosition();
//		if(post==null){
//			post = personnel.getProtocolPosition();
//		}
//		dutyHistoryModel.setID(Integer.valueOf(dictionaryService.findByCode(post).getEnName()));//职务ID
////		dutyHistoryModel.setMemo(Memo);//备注
////		dutyHistoryModel.setMgrID(MgrID);//部门经理ID
////		dutyHistoryModel.setOldSuperID(OldSuperID);//旧业务主管ID
//		dutyHistoryModel.setStateID(1);//状态
//		dutyHistoryModel.setSuperID((String) employeeVo.get("EmployeeID"));//主管ID
//		dutyHistoryModels.add(dutyHistoryModel);
		//return null;
	//}

	/**
	 * 教育经历信息集合
	 * @return List<EducationHistoryModel>
	 */
//	private List<EducationHistoryModel> addEducationHistoryModellList(Personnel personnel){
//		List<Educational> listEdu = educationalMapper.selectByPersonnelId(personnel.getId());
//		List<EducationHistoryModel> educationHistoryModels = new ArrayList<>();
//
//		if(listEdu!=null&&listEdu.size()>0){
//			for(Educational edu : listEdu){
//				EducationHistoryModel educationHistoryModel = new EducationHistoryModel();
//				educationHistoryModel.setBeginDate(FomartTimeUtil.ymdUtil(edu.getStartTime()));//开始日期
//   			    //educationHistoryModel.setEducationHistoryID(EducationHistoryID);//教育经历ID
//				//educationHistoryModel.setEducationMemo(EducationMemo);//备注
//				//educationHistoryModel.setEmployeeID(EmployeeID);//员工代码
//				educationHistoryModel.setEndDate(FomartTimeUtil.ymdUtil(edu.getEndTime()));//结束日期
//				educationHistoryModel.setMajorName(edu.getSpecialty());//专业名称
//				educationHistoryModel.setSchoolName(edu.getSchool());//学校或科系
//
//				educationHistoryModels.add(educationHistoryModel);
//			}
//		}
//
//
//		return educationHistoryModels;
//	}

	/**
	 * 工作履历详细附件信息集合
	 * @return List<ResumeDetailFileModel>
	 */
//	private List<ResumeDetailFileModel> addResumeDetailFileModelList(){
//
//		List<ResumeDetailFileModel> resumeDetailFileModels = new ArrayList<>();
//		ResumeDetailFileModel resumeDetailFileModel = new ResumeDetailFileModel();
//		resumeDetailFileModels.add(resumeDetailFileModel);
//
//		return resumeDetailFileModels;
//	}

	/**
	 * 培训经历详细附件信息集合
	 * @return List<TrainingDetailFileModel>
	 */
//	private List<TrainingDetailFileModel> addTrainingDetailFileModelList(){
//
//		List<TrainingDetailFileModel> trainingDetailFileModels = new ArrayList<>();
//		TrainingDetailFileModel trainingDetailFileModel = new TrainingDetailFileModel();
//		trainingDetailFileModels.add(trainingDetailFileModel);
//
//		return trainingDetailFileModels;
//	}

	/**
	 * 教育经历详细附件信息集合
	 * @return List<EducationDetailFileModel>
	 */
//	private List<EducationDetailFileModel> addEducationDetailFileModelList(){
//
//		List<EducationDetailFileModel> educationDetailFileModels = new ArrayList<>();
//		EducationDetailFileModel educationDetailFileModel = new EducationDetailFileModel();
//		educationDetailFileModels.add(educationDetailFileModel);
//
//		return educationDetailFileModels;
//	}


	/**
	 * 职业证详细附件信息集合
	 * @return List<CertificateDetailFileModel>
	 */
//	private List<CertificateDetailFileModel> addCertificateDetailFileModelList(){
//
//		List<CertificateDetailFileModel> certificateDetailFileModels = new ArrayList<>();
//		CertificateDetailFileModel certificateDetailFileModel = new CertificateDetailFileModel();
//		certificateDetailFileModel.setID(98);
//		certificateDetailFileModel.setCertificateDetailFileID(98);
//		certificateDetailFileModel.setFileName("身份证.png");
//		certificateDetailFileModel.setPath("https://elearning.juntianbroker.com/mediaPlay/2018-46/10625775172331765761542173196502-802855772.jpeg");
//		certificateDetailFileModel.setCustomerFileName("身份证.png");
//		certificateDetailFileModels.add(certificateDetailFileModel);
//
//		return certificateDetailFileModels;
//	}

	/**
	 * 职业证信息集合
	 * @return List<CertificateModel>
	 */
//	private List<CertificateModel> addCertificateModelList(Personnel personnel){
//
//		List<CertificateModel> certificateModels = new ArrayList<>();
//		if(personnel.getBankBranch()!=null){
//			CertificateModel certificateModel = new CertificateModel();
//			certificateModel.setID(17);
//			certificateModel.setCertificateID(17);
//			certificateModel.setCertificateNo(personnel.getBankBranch());
//			certificateModel.setCertificateMemo(DateUtil.toSeconds(new Date()));
//			certificateModels.add(certificateModel);
//		}
////		MER-3408---银行卡会传2个
//		if(personnel.getChinaCiticBank()!=null){
//			CertificateModel certificateModel = new CertificateModel();
//			certificateModel.setCertificateName("1");
//			certificateModel.setCertificateID(1);
//			certificateModel.setCertificateNo(personnel.getChinaCiticBank());
//			certificateModel.setCertificateMemo(DateUtil.toSeconds(new Date()));
//			certificateModels.add(certificateModel);
//		}
//		if(personnel.getPolitical()!=null){
//			Dictionary dict = dictionaryService.findByCode(personnel.getPolitical());
//			if(dict!=null){
//				CertificateModel certificateModel = new CertificateModel();
//				certificateModel.setCertificateID(98);
//				certificateModel.setCertificateName("代理人政治面貌");
//				certificateModel.setID(98);
//				certificateModel.setCertificateNo(dict.getValue());
//				certificateModels.add(certificateModel);
//			}
//		}
//
//		return certificateModels;
//	}


	/**
	 * 工作履历信息集合
	 * @return List<ResumeModel>
	 */
//	private List<ResumeModel> addResumeModelList(Personnel personnel){
//		List<WorkExperience> workList = workExperienceMapper.selectByPersonnelId(personnel.getId());
//		List<ResumeModel> resumeModels = new ArrayList<>();
//
//		if(workList!=null && workList.size()>0){
//			for(WorkExperience work:workList){
//				ResumeModel resumeModel = new ResumeModel();
//				resumeModel.setCompany(work.getCompany());//工作单位
//				resumeModel.setDuty(work.getPost());//职务
//				resumeModel.setDutyMemo(work.getOccupation());//执掌业务
////				resumeModel.setEmployeeID(EmployeeID);//员工代码
////				resumeModel.setResume_Memo(Resume_Memo);//备注
////				resumeModel.setResumeID(ResumeID);//工作履历ID
//				resumeModel.setWorkBeginDate(FomartTimeUtil.ymdUtil(work.getStartTime()));//工作开始时间
//				resumeModel.setWorkEndDate(FomartTimeUtil.ymdUtil(work.getEndTime()));//工作结束时间
//
//				resumeModels.add(resumeModel);
//			}
//		}
//
//
//		return resumeModels;
//	}

	/**
	 * 培训经历信息集合
	 * @return List<TrainingHistoryModel>
	 */
//	private List<TrainingHistoryModel> addTrainingHistoryModelList(){
//
//		List<TrainingHistoryModel> trainingHistoryModels = new ArrayList<>();
//
//		TrainingHistoryModel trainingHistoryModel = new TrainingHistoryModel();
////		trainingHistoryModel.setCertificateName(CertificateName);//证书名称
////		trainingHistoryModel.setCertificateNo(CertificateNo);//证书号码
////		trainingHistoryModel.setEmployeeID(EmployeeID);//员工代码
////		trainingHistoryModel.setSpeciality(Speciality);//专长
////		trainingHistoryModel.setTrainingBeginDate(TrainingBeginDate);//培训开始时间
////		trainingHistoryModel.setTrainingContent(TrainingContent);//培训内容
////		trainingHistoryModel.setTrainingEndDate();//培训结束时间
////		trainingHistoryModel.setTrainingHistoryID(TrainingHistoryID);//培训经历ID
////		trainingHistoryModel.setTrainingMemo(TrainingMemo);//培训备注
////		trainingHistoryModel.setTrainingName(TrainingName);//培训名称
//
//		trainingHistoryModels.add(trainingHistoryModel);
//
//		return trainingHistoryModels;
//	}



//	@Override
//	@Transactional
//	public Response<String> recruitTimeTask() {
//		Response<String> res=new Response<>(Response.SUCCESS, "操作成功");
//		List<String> ids=personnelMapper.selectPersonnelIdList();
//		if(!ListUtils.isEmpty(ids)){
//			logger.info("跑批数据人员ID："+ JSON.toJSONString(ids));
//		}
//		PersonnelLog personnelLog=new PersonnelLog();
//		int totalNum=0;
//		int successNum=0;
//		personnelLog.setStartTime(new Date());
//		try {
//			for (String id : ids) {
//				InterviewAction interviewAction = personnelMapper.selectInterviewActionById(id);
//				if(interviewAction == null){
//					continue;
//				}
//				long day = getInterval(interviewAction.getStartTime(),new Date());
//				if(day>30){
//					totalNum++;
//					int inde = personnelMapper.updatePersonnelStatus(id, "-1");
//					if(inde>0){
//					successNum++;
//					InterviewAction interviewAction1 = new InterviewAction();
//					interviewAction1.setId(interviewAction.getId());
//					/*--影响前端数据显示
//					interviewAction1.setEndTime(new Date());
//					*/
//					interviewAction1.setUpdateTime(new Date());
//					interviewAction1.setProcessingStatus("2");
//					interviewAction1.setProcessingPerson("30");
//					interviewAction1.setProcessingName("30天跑批");
//					interviewActionMapper.updateById(interviewAction1);
//					/*interviewActionMapper.updateInterviewActionByIdCode(id,String.valueOf(interviewAction.getFlowItemId()),"2",null);*/
//					}
//				}
//			}
//		} catch (Exception e) {
//			personnelLog.setRemark(e.getMessage());
//			res.setMsg("操作失败!");
//			res.setCode(Response.ERROR);
//		}
//		personnelLog.setTotalNum(totalNum);
//		personnelLog.setSuccessNum(successNum);
//		personnelLog.setEndTime(new Date());
//		personnelLog.setGmtCreate(new Date());
//		personnelLog.setId(Idfactory.generate());
//		int insert = personnelMapper.insertPersonnelLog(personnelLog);
//		if(insert<0){
//			res.setCode(Response.ERROR);
//			res.setMsg("跑批数据插入log失败");
//		}
//		return res;
//	}
	/**
	 * 计算两日期相差天数
	 * @param begin_date
	 * @param end_date
	 * @return
	 * @throws Exception
	 */
 long getInterval(Date begin_date, Date end_date) throws Exception{
	    long day = 0;
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    if(begin_date != null){
	        String begin = sdf.format(begin_date);
	        begin_date  = sdf.parse(begin);
	    }
	    if(end_date!= null){
	        String end= sdf.format(end_date);
	        end_date= sdf.parse(end);
	    }
	    day = (end_date.getTime()-begin_date.getTime())/(24*60*60*1000);
	    return day;
	}



	@Override
	public Personnel selectPersonnerById(String id) {
		return personnelMapper.selectPersonnerOneById(id);
	}



//	@Override
//	public PageInfo<Personnel> SelectPersonnerEl(Page<Personnel> page) {
//		PageInfo<Personnel> pageInfo = new PageInfo<Personnel>();
//		pageInfo.setRecords(personnelMapper.SelectPersonnerEl(page) );
//		int total = personnelMapper.SelectPersonnerElCount();
//		pageInfo.setPageNo(page.getCurrent());
//		pageInfo.setPageSize(page.getSize());
//		pageInfo.setPageTotal(PageUtil.getPages(page.getSize(),total));
//		pageInfo.setPageRecords(page.getTotal());
//		return pageInfo;
//	}




	public String selectContractLastRecord(String preCode) {
		return presentRecordMapper.selectContractLastRecord(preCode);
	}




	@Transactional
	public Response<String> insertPresentRecord(PresentRecord presentRecord) {
		Response<String> res=new Response<>(Response.SUCCESS, "插入成功");
		presentRecord.setId(idGenerator.generate());
		Integer insert = presentRecordMapper.insert(presentRecord);
		if(insert==0){
			res.setCode(Response.ERROR);
			res.setMsg("插入失败请重试");
		}
		return res;
	}


	public int dele(String id) {
		return personnelMapper.dele(id);
	}


	public int checkUrl(long personnelId) {
 		Personnel personnel= personnelMapper.selectByPersonnelId(personnelId);
 		if(personnel != null){
			if("-1".equals(personnel.getPersonnelStatus())){
				return 1;
			}else {
				return 0;
			}
		}else{
			return 1;
		}
	}
	/**
	 * @Author Mr.chen
	 * @Description
	 * //TODO 用于学习平台中的培训审核完成
	 * @Date
	 * @Param
	 * @return
	 **/

	@Transactional
	public Response<String> studyComplete(StudyCompleteVo studyCompleteVo) {
 	//查询当前人的状态
		Map map1 = new HashMap();
//		map1.put("cellphone",studyCompleteVo.getPhone());
//		map1.put("name",studyCompleteVo.getName());
		map1.put("id",studyCompleteVo.getPersonnelId());
		List<Personnel> personnels = personnelMapper.selectByMap(map1);


		//		检查人员当前状态在培训的有效状态
		if(personnels == null || !"4".equals(personnels.get(0).getPersonnelStatus())){
			return new Response<>(Response.ERROR,"无此人员信息！");
		}

		//--根据状态更新数据
		//--更新面试流程表和人员表的不同状态
		if("1".equals(studyCompleteVo.getCheckResult())) {
			InterviewActionVo interviewActionVo = new InterviewActionVo();
			interviewActionVo.setId(Idfactory.generate());
			interviewActionVo.setPersonnelId(""+personnels.get(0).getId());
			interviewActionVo.setStartTime(new Date());
			interviewActionVo.setFlowItemId("6");

			InterviewAction updataInterviewAction = new InterviewAction();
			updataInterviewAction.setPersonnelId(personnels.get(0).getId());
			updataInterviewAction.setProcessingPerson(studyCompleteVo.getId());
			updataInterviewAction.setEndTime(new Date());
			updataInterviewAction.setProcessingStatus(studyCompleteVo.getCheckResult());
			updataInterviewAction.setProcessingPerson(studyCompleteVo.getId());
			updataInterviewAction.setProcessingName(studyCompleteVo.getChannel());
			interviewActionMapper.completeStudy(updataInterviewAction);
			int num = interviewActionMapper.insertInterviewAction(interviewActionVo);
			if(num > 0){
				personnelMapper.updatePersonnelStatus(interviewActionVo.getPersonnelId(), interviewActionVo.getFlowItemId());
			}else{
				return new Response<>(Response.ERROR,"操作失败！");
			}
			return new Response<>(Response.SUCCESS,Response.SUCCESS_MESSAGE);
		}else{
			updatePersonnerStaus(""+personnels.get(0).getId(),"-1");
			InterviewAction interviewAction = new InterviewAction();
			interviewAction.setPersonnelId(personnels.get(0).getId());
			interviewAction.setFlowItemId(4L);
			InterviewAction interviewAction1 = interviewActionMapper.selectOne(interviewAction);
			if(interviewAction1 != null){
				interviewAction1.setProcessingPerson(studyCompleteVo.getId());
				interviewAction1.setProcessingName(studyCompleteVo.getChannel());
				interviewAction1.setUpdateTime(new Date());
				interviewAction1.setEndTime(new Date());
				interviewAction1.setProcessingStatus("2");
				interviewActionMapper.updateById(interviewAction1);
			}else{
				logger.info("学习平台更改数据有误~",studyCompleteVo);
			}
			/*updateInterviewActionByIdCode(""+personnels.get(0).getId(),"4","2");*/
			return new Response<>(Response.SUCCESS,Response.SUCCESS_MESSAGE);
		}
	}

	@Transactional
	public String saveLink(String url,String id){
		ShareInfo shareInfo = new ShareInfo();
		/*Account account = SessionContextUtils.getCurrentUser();*/
		shareInfo.setShareUrl(url);
		shareInfo.setCreateUser(id);
		return shareInfoService.addShare(shareInfo).getData().getShareUrl();
	}
	@Autowired
	private ISysDictTypeService dictionaryService;

	@Autowired
	private SysLoginUserMapper sysLoginUserMapper;

	@Transactional
	public Response<String> saveInfo1(PersonnelVo1 personnelVo1) {

		Personnel personnelExpVo = personnelVo1.getPersonnel();
		List<Educational> educationalList = personnelVo1.getEducationalList();
		List<WorkExperience> workExperienceList = personnelVo1.getWorkExperienceList();
		Personnel personnel = setPersonnel(personnelExpVo);
		Response<String> resp = new Response<String>();
		//判断当前流程中是否存在该增员人才
		Personnel searchPersonnel = new Personnel();
		//根据手机号和姓名验证是否已经提交
		//searchPersonnel.setName(personnelExpVo.getName());
//		searchPersonnel.setCellphone(personnelExpVo.getCellphone());
//		List<Personnel> personnelList = personnelMapper.getPersonnelByRecord(searchPersonnel);
//		if(personnelList!=null&&personnelList.size()>0){
//			resp.setCode(Response.ERROR);
//			resp.setMsg("您已提交成功，报聘流程进行中，请敬待通知");
//			return resp;
//		}

		Long personnelId = idGenerator.generate();
		personnel.setId(personnelId);
		//获取当前人才的状态为录入则执行操作否则不执行
		//String personnelStatus = personnelMapper.selectPersonnelStatusById(personnelId);
		Personnel personnelFir = personnelMapper.selectById(personnelExpVo.getId());
		if(personnelFir==null){
			personnelFir = new Personnel();
		}
		personnelFir.setId(personnelId);
		personnelFir.setPersonnelStatus("1");
		personnelFir.setCreateTime(new Date());
		personnelFir.setCreateUser(personnelExpVo.getCreateUser());

//		增加推荐人姓名-10/15
		try {
			EmployeeNew searchEmployeeNew = new  EmployeeNew();
			searchEmployeeNew.setPresentCode(personnelExpVo.getCreateUser());
			List<HashMap<String, Object>>  employeeVoList = null;//todo customerService.queryListNew(searchEmployeeNew);
			personnelFir.setCreateUserName((String)employeeVoList.get(0).get("EmployeeName"));
		}catch (Exception e){
			logger.error(personnelExpVo.getCreateUser()+"新增推荐人姓名失败。");
		}

			 /*personnelFir.setName(personnelExpVo.getName());
			 personnelFir.setCellphone(personnelExpVo.getCellphone());*/
		personnelMapper.insertInfo(personnelFir);

		if("1".equals(personnelFir.getPersonnelStatus())){
			//根据职级设置初审或复审
			personnel.setPersonnelStatus(getPersonnelStatus(personnelExpVo));
			personnel.setUpdateTime(new Date());
			personnel.setUpdateUser(personnelId);
			personnel.setTotalWorkTime(personnelExpVo.getTotalWorkTime());
			personnel.setNativeWorkTime(personnelExpVo.getNativeWorkTime());

			InterviewAction interviewAction = new InterviewAction();
			interviewAction.setPersonnelId(personnelId);
			interviewAction.setId(idGenerator.generate());

			String updateStatus = getPersonnelStatus(personnelExpVo);
			interviewActionMapper.updateByPrimaryPersonnel(interviewAction);
			interviewAction.setFlowItemId(Long.valueOf(updateStatus));
			if("003".equals(personnel.getChannel())){
//				EmployeeVo resultEmployee=null;
				Response<Map<String, List<DictVo>>> dictResp = dictionaryService.findByCodes("rank");
				List<DictVo> dictList = dictResp.getData().get("rank");
				if(dictList!=null&&dictList.size()>0&&personnelFir.getCreateUser()!=null){
					SysLoginUser resultEmployee =  sysLoginUserMapper.selectUserByAgentCode(personnelFir.getCreateUser());
//					resultEmployee= employeeUtil.getFirstTrialEmployee(employeeVo, personnel.getProtocolPosition(), dictList);
					if(resultEmployee!=null){
						interviewAction.setProcessingName(resultEmployee.getUser_name());
						interviewAction.setProcessingPerson(resultEmployee.getC_emp_cde2());
					}
				}
			}
			if(personnelFir.getCreateUser()!=null){
				SysLoginUser resultEmployee =  sysLoginUserMapper.selectUserByAgentCode(personnelFir.getCreateUser());
				if(resultEmployee!=null){
					personnel.setCreateUserName(resultEmployee.getUser_name());
				}
			}
			String url = "";
			String content = "";

			interviewActionMapper.insert(interviewAction);

			//录入结束
			//人员先插入基本信息，再更新信息
			personnelMapper.updateByPrimaryKeySelective(personnel);
			//工作经历录入  先删除，后插入。
			if(workExperienceList != null && workExperienceList.size() > 0){
				workExperienceMapper.deleteByPersonnelId(personnelId);
					for(WorkExperience workExperience :  workExperienceList){
						workExperience.setPersonnelId(personnelId);
						workExperience.setId(idGenerator.generate());
						workExperienceMapper.insertSelective(workExperience);
					}
			}
            if(educationalList !=null && educationalList.size()>0){
                for(Educational educational :  educationalList){
                    educational.setPersonnelId(personnelId);
                    educational.setId(idGenerator.generate());
                    educationalMapper.insertSelective(educational);
                }
            }
			resp.setCode(Response.SUCCESS);
			resp.setMsg("更新个人信息成功");
			return resp;
		}

		resp.setCode(Response.SUCCESS);
		resp.setMsg("请勿重复录入");
		return resp;
	}

//	@Override
//	public List<Role> roleType(Long accountId) {
//		List<Role> roleList = null;
//		Response<Map<String,List<Dictionary>>> response = dictionaryService.findByCodes("er_role");
//		String trainRoleName = null;
//		String checkRoleName = null;
//		if(response != null && response.getData() != null){
//			Map<String,List<Dictionary>> map = response.getData();
//			List<Dictionary> list = map.get("er_role");
//			if(list != null && list.size() > 0 ){
//				for (Dictionary dictionary:
//				list) {
//					if("er_train_role".equals(dictionary.getCode())){
//						trainRoleName = dictionary.getValue();
//
//					};
//					if("er_not_train_role".equals(dictionary.getCode())){
//						checkRoleName =	dictionary.getValue();
//					};
//				}
//			}else{
//				throw new RuntimeException("查询字典表失败！");
//			}
//		}else{
//			throw new RuntimeException("查询字典表失败！");
//		}
//		Response<List<Role>> response1 = accountService.findRolesByAccount(accountId);
//		if(response1 != null && response1.getData() !=null ){
//			List<Role> roles = response1.getData();
//			if(roles.size() > 0){
//				for (Role role:
//				roles) {
//					if(trainRoleName.equals(role.getRoleName())){
//						if(roleList != null){
////							Enabled字段标识是否是培训审核角色（true:培训审核角色，false:非培训审核角色）
//							role.setEnabled(true);
//							roleList.add(role);
//						}else{
//							roleList = new ArrayList<>();
//							role.setEnabled(true);
//							roleList.add(role);
//						}
//					}
//					if(checkRoleName.equals(role.getRoleName())){
//						if(roleList != null){
//							role.setEnabled(false);
//							roleList.add(role);
//						}else{
//							roleList = new ArrayList<>();
//							role.setEnabled(false);
//							roleList.add(role);
//						}
//					}
//				}
//			}else{
//				logger.info("此人员无任何角色配置！");
//			}
//		}else{
//			throw new RuntimeException("获取角色信息失败！");
//		}
//		return roleList;
//	}

	@Override
	public List<PersonnelResultVo> personnelList(PersonnelSearchVo personnelSearchVo) {
    // 		获取个人信息
     //	LoginUser loginUser = tokenService.getLoginUser();// 获取用户对象

		logger.info("=============",personnelMapper);

        List<PersonnelResultVo> list = personnelMapper.personnelList(personnelSearchVo);
		logger.info("______________时间2:"+new Date());
		//getFull(list);
		logger.info("______________时间3:"+new Date());
		return list;
	}

//    private void getFull(List<PersonnelResultVo> list) {
//        if(list != null && list.size() > 0 ){
//            for (PersonnelResultVo personnelResultVo:
//            list) {
//            	logger.info("开始personnelResultVo:"+JsonUtils.objectToJson(personnelResultVo));
////              获取推荐人姓名
//                if(personnelResultVo != null && personnelResultVo.getCreateUser() != null){
//					EmployeeVo employeeVo = null;
//					if(redisUtil.exists("ER_EmployeeVo_"+personnelResultVo.getCreateUser())){
//						employeeVo = (EmployeeVo)redisUtil.get("ER_EmployeeVo_"+personnelResultVo.getCreateUser());
//					}else{
//						employeeVo = customerService.queryEmployeeByPresentCode(personnelResultVo.getCreateUser());
//						redisUtil.set("ER_EmployeeVo_"+personnelResultVo.getCreateUser(),employeeVo,120L);
//					}
//
//					logger.info("2");
//                    if(null != employeeVo){
//                        personnelResultVo.setCreateUser(employeeVo.getEmployeeName());
//                    }
//
//					OrganizationChart organizationChart = null;
//                    if(redisUtil.exists("ER_Organization_"+personnelResultVo.getCompanyId())){
//						organizationChart = (OrganizationChart)redisUtil.get("ER_Organization_"+personnelResultVo.getCompanyId());
//					}else{
//						organizationChart = organizationChartService.getOrganizationChart(personnelResultVo.getCompanyId()).getData();
//						redisUtil.set("ER_Organization_"+personnelResultVo.getCompanyId(),organizationChart,1800L);
//					}
//					if(organizationChart!=null && "6".equals(organizationChart.getLayer())){
//						OrganizationChart organizationChart1 = null;
//					if(redisUtil.exists("ER_Organization_"+organizationChart.getParentCode())){
//						organizationChart1 = (OrganizationChart)redisUtil.get("ER_Organization_"+organizationChart.getParentCode());
//					}else{
//						organizationChart1 = organizationChartService.getOrganizationChart(organizationChart.getParentCode()).getData();
//						redisUtil.set("ER_Organization_"+organizationChart.getParentCode(),organizationChart,1800L);
//					}
//					personnelResultVo.setCreateUserOrgName(organizationChart1.getCnName());
//				}
//
//
//					logger.info("3");
//					Integer layer = 5;
//					try {
//						layer = Integer.valueOf(organizationChart.getLayer());
//					}catch (Exception e){
//						if(organizationChart != null){
//							logger.error(organizationChart.getCode()+"转换失败"+organizationChart.getLayer());
//						}
//					}
//
//					while (organizationChart != null && layer> 3){
//						logger.info("1");
//						String code = organizationChart.getParentCode();
//						organizationChart = null;
//						if(redisUtil.exists("ER_Organization_"+code)){
//							organizationChart = (OrganizationChart)redisUtil.get("ER_Organization_"+code);
//							if(!code.equals(organizationChart.getId())){
//								organizationChart = organizationChartService.getOrganizationChart(code).getData();
//								redisUtil.set("ER_Organization_"+code,organizationChart,1800L);
//							}
//						}else{
//							organizationChart = organizationChartService.getOrganizationChart(code).getData();
//							redisUtil.set("ER_Organization_"+code,organizationChart,1800L);
//						}
//
//						if(organizationChart != null && "3".equals(organizationChart.getLayer())){
//							personnelResultVo.setCompanyName(organizationChart.getCnName());
//							personnelResultVo.setCompanyId(organizationChart.getCode());
//						}
//
//						try {
//							layer = Integer.valueOf(organizationChart.getLayer());
//						}catch (Exception e){
//							if(organizationChart != null){
//								logger.error(organizationChart.getCode()+"转换失败"+organizationChart.getLayer());
//							}
//						}
//
//					}
////					3  分公司   5事业部  6营业部
//
//                }
////              获取流程信息
//                switch (personnelResultVo.getPersonnelStatus()){
//                    case "2": personnelResultVo.setPersonnelStatusName("初审");break;
//                    case "3": personnelResultVo.setPersonnelStatusName("复审");break;
//                    case "4": personnelResultVo.setPersonnelStatusName("培训审核");break;
//                    case "6":
//                        personnelResultVo.setPersonnelStatusName("1".equals(personnelResultVo.getIsSend()) ? "已转发":"签约转发");break;
//                    case "7": personnelResultVo.setPersonnelStatusName(personnelResultVo.getAgentCode() == null?
//							"2".equals(personnelResultVo.getCheckResult())?"核查未通过":
//									"1".equals(personnelResultVo.getCheckResult())? "核查通过":"待核查":"已完成");break;
//                }
//                int b = 0;
//				logger.info("结束personnelResultVo:"+JsonUtils.objectToJson(personnelResultVo));
//            }
//        }
//    }

//    @Override
//    public List<ExportPersonnelVo> exportPersonnelExcel(PersonnelSearchVo personnelSearchVo) {
//        // 		获取个人信息
//        Account account = (Account) redisUtil.getUserInfo();
//
//        if(personnelSearchVo!=null ){
////            Response<List<OrganizationChartVo>> response1 = organizationChartService.getOrganizationChartsByAccount(""+account.getId());
//            Response<List<OrganizationChartVo>> response1 = null;
//            if(response1==null){
//                if(redisUtil.exists("Er_OrganizationChart"+account.getId())){
//                    response1 = (Response<List<OrganizationChartVo>>) redisUtil.get("Er_OrganizationChart"+account.getId());
//                    new Thread(
//                            new Runnable() {
//                                @Override
//                                public void run() {
//                                    Thread.currentThread().setName("刷新组织架构缓存"+account.getId());
//                                    logger.info(Thread.currentThread().getName()+"线程刷新组织架构缓存开始执行"+account.getId());
//                                    Response<List<OrganizationChartVo>> response2 = organizationChartService.getOrganizationChartsByAccount(""+account.getId());
//                                    redisUtil.set("Er_OrganizationChart"+account.getId(),response2);
//                                }
//                            }
//                    ).start();
//                }else{
//                    response1 = organizationChartService.getOrganizationChartsByAccount(""+account.getId());
//                    redisUtil.set("Er_OrganizationChart"+account.getId(),response1);
//                }
//            }
//
//            if(response1!=null && "1".equals(response1.getCode())){
//                getPersonnelSearchVo(response1,personnelSearchVo);
//            }else{
//                logger.info("personnelList获取组织架构失败:"+ JsonUtils.objectToJson(personnelSearchVo));
//                return null;
//            }
//        }
//        if(personnelSearchVo.getCompanyId() != null || personnelSearchVo.getCompanyId1() != null){
////		获取个人所有权限的公司
//            Response<String> response = organizationChartService.getSubCodeByAccount(account);
//            if(response != null){
//                String orgId = response.getData();
//                String[] orgIds = orgId.split(",");
//                if(orgIds != null && orgIds.length > 0){
//                    List list = new ArrayList<>();
//                    for (String str:
//                            orgIds) {
//                        list.add(str);
//                    }
//                    personnelSearchVo.setSubOrgId(list);
//                }
//            }else{
//                logger.error("组织权限获取失败");
//                throw new RuntimeException("组织权限获取失败。");
//            }}
//
//        List<ExportPersonnelVo> list = personnelMapper.exportPersonnelExcel(personnelSearchVo);
//
//		//		线程优化数据的获取。
//		Map map = new HashMap();
//		map.put("dictionaryService",dictionaryService);
//		map.put("customerService",customerService);
//		map.put("attachmentService",attachmentService);
//		map.put("familyMemberMapper",familyMemberMapper);
//		map.put("workExperienceMapper",workExperienceMapper);
//		map.put("organizationChartService",organizationChartService);
//        map.put("redisUtil",redisUtil);
//		logger.info(new Date()+"=======线程开始=======");
//		Future<List<ExportPersonnelVo>> futures = executorService.submit(new ExportPersonnelHandleTask(list,executorService,map));
//		List<ExportPersonnelVo> ExportExcel=new ArrayList<>();
//		try {
//			ExportExcel = futures.get();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}
//		logger.info(new Date()+"=======线程结束=======");
//
//
////        getFull(list);
//        return list;
//    }

//    private void getPersonnelSearchVo(Response<List<OrganizationChartVo>> response, PersonnelSearchVo personnelSearchVo) {
//		if(response != null && response.getData() != null && response.getData().size() > 0){
//			List<OrganizationChartVo> list = response.getData();
//			String companyIds = null;
//			if(list == null || list.size() <= 0){
//				return ;
//			}
//			for (OrganizationChartVo organizationChartVo1:
//			list) {
//				if(personnelSearchVo.getCompanyId() != null){
////					分公司刷选：获取所有下级code
//					OrganizationChart organizationChart = null;
//					if(redisUtil.exists("ER_Org"+personnelSearchVo.getCompanyId()))
//						organizationChart = (OrganizationChart)redisUtil.get("ER_Org"+personnelSearchVo.getCompanyId());
//					else{
//						organizationChart = organizationChartService.getOrganizationChart(""+personnelSearchVo.getCompanyId()).getData();
//						redisUtil.set("ER_Org"+personnelSearchVo.getCompanyId(),organizationChart);
//					}
//					if(organizationChart.getSubCode().contains(organizationChartVo1.getCode()))
//						companyIds = companyIds==null?organizationChartVo1.getCode():companyIds+","+organizationChartVo1.getCode();
//				}else
//					companyIds = companyIds==null?organizationChartVo1.getCode():companyIds+","+organizationChartVo1.getCode();
//			}
//			logger.info("companyIds："+companyIds);
//			String[] strings =companyIds.split(",");
//			Set<String> set = new HashSet<>();
//			for (int i = 0; i < strings.length; i++) {
//				set.add(strings[i]);
//			}
//			companyIds = null;
//			for (String string : set) {
//				companyIds = companyIds==null?string:companyIds+","+string;
//			}
//			logger.info("companyIds："+companyIds);
//			personnelSearchVo.setCompanyId(companyIds);
//		}
//	}


//	@Override
//	public PersonnelFlowVo personnelFlow(String personnelId) {
//		PersonnelFlowVo personnelFlowVo = new PersonnelFlowVo();
//		List<FlowItem> list = null;
//		list = (List<FlowItem>)redisUtil.get("Er_Flow");
//		if(list == null || list.size() <= 0){
//			logger.info("redis获取（FlowItem）数据失败");
//			list = personnelMapper.selectFlowItem1();
//			redisUtil.set("Er_Flow",list,Long.valueOf(60));
//		}
//		List<FlowItemActionPersonnerVo> flowItemPersonner = personnelMapper.selectFlowPersonner(personnelId);
//		personnelFlowVo = getPersonnelFlowVo(list,flowItemPersonner,personnelFlowVo);
//		return personnelFlowVo;
//	}

	public PersonnelFlowVo getPersonnelFlowVo(List<FlowItem> flowItemList , List<FlowItemActionPersonnerVo> flowItemPersonner, PersonnelFlowVo personnelFlowVo){
		personnelFlowVo.setFlowItemPersonner(flowItemPersonner);
		if(flowItemList != null && flowItemPersonner != null){
			int max = 0;
//			获取最大流程号
			for (FlowItemActionPersonnerVo flowtIemActionPersonnerVo:
			flowItemPersonner) {
				int flow = 0;
				try {
					flow = Integer.valueOf(flowtIemActionPersonnerVo.getMoudleName());
				}catch (Exception e){
					logger.error("流程序号转换失败");
				}
				if(flow>max){
					max = flow;
				}
			}

//		刷掉未走面试
			List<FlowItem> flowItemList1 = new ArrayList<>();
			for (FlowItem flowItem:
			flowItemList) {
				Boolean on = false;
				if(Integer.valueOf(flowItem.getMoudleName())<max){
					for (FlowItemActionPersonnerVo flowItemActionPersonnerVo:
							flowItemPersonner) {
						if(flowItem.getMoudleName().equals(flowItemActionPersonnerVo.getMoudleName())){
							flowItemList1.add(flowItem);
						}
					}
				}else{
					flowItemList1.add(flowItem);
				}
			}
			personnelFlowVo.setFlowList(flowItemList1);
		}
		return personnelFlowVo;
	}

    @Override
    public Check getImgCheck(String personnelId) {
     Check check =  checkMapper.selectByPersonnelId(Long.valueOf(personnelId));
     return check;
    }


	public int insertCheck(Check check) {
 		logger.info("入参check:"+JsonUtil.objectToJson(check));
		int num = 0;
		try {
			check.setId(idGenerator.generate());
 			check.setCreateTime(new Date());
			num = checkMapper.insert(check);
		}catch (Exception e){
			logger.error("插入审核结果失败：",e);
		}
		return num;
	}

/**
 * @Author Mr.chen
 * @Description
 * //TODO 后台关闭功能
 * @Date
 * @Param
 * @return
 **/
	public Response<String> closePersonnel(String personnelId) {
		Response<String> response = new Response<>(Response.ERROR,"Just进入方法");
		logger.info("closePersonnel入参:"+personnelId);
		Personnel personnel = personnelMapper.selectById(personnelId);
		Boolean canClose = checkCanCloseByPersonnelId(personnel);
		if(canClose){
			response.setData("正在更新...");
			personnel.setIsClose(1);
			logger.info("closePersonnel"+ JSON.toJSONString(personnel));
			int updateNum = personnelMapper.updateAllColumnById(personnel);
			if(updateNum>0){
				response = new Response<>(Response.SUCCESS,Response.SUCCESS_MESSAGE);
			}else{
				response =  new Response<>(Response.ERROR,"更新失败");
			}
		}else {
			response =  new Response<>(Response.ERROR,"更新失败");
		}
		return response;
	}

	/**
	 * @Author Mr.chen
	 * @Description
	 * //TODO 检查是否可以关闭(状态未关闭且未完成状态)
	 * @Date
	 * @Param
	 * @return
	 **/
	private Boolean checkCanCloseByPersonnelId(Personnel personnel) {
		logger.info("checkCanCloseByPersonnelId入参:"+ JSON.toJSONString(personnel));
		if(personnel != null){
			if((personnel.getIsClose()!=null && personnel.getIsClose()==1)||!StringUtils.isEmpty(personnel.getAgentCode())){
				logger.info("checkCanCloseByPersonnelId返回值false",personnel);
				return false;
			}else{
				logger.info("checkCanCloseByPersonnelId返回值true",personnel);
				return true;
			}
		}
		logger.info("checkCanCloseByPersonnelId返回值false",personnel);
		return false;
	}

//    @Override
//    public Response<Boolean> isHeadCompany() {
//        Response<Boolean> response = new Response<>(Response.ERROR,"Just--进入方法");
//        Account account = (Account) redisUtil.getUserInfo();
//        String companyId = account.getCompanyId();
//		OrganizationChart organizationChart = organizationChartService.getOrganizationChart(companyId).getData();
//		logger.info("isHeadCompany获取到的公司"+ JSON.toJSONString(organizationChart));
//        if(organizationChart != null && "1".equals(organizationChart.getLayer())){
//            response.setCode(Response.SUCCESS);
//            response.setMsg(Response.SUCCESS_MESSAGE);
//			logger.info("isHeadCompany返回"+ JSON.toJSONString(response));
//            response.setData(true);
//        }else{
//            response.setCode(Response.SUCCESS);
//            response.setMsg(Response.SUCCESS_MESSAGE);
//            response.setData(false);
//			logger.info("isHeadCompany返回"+ JSON.toJSONString(response));
//        }
//        return response;
//    }
}
