package com.fulan.application.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import com.bpic.common.core.domain.entity.SysLoginUser;
import com.bpic.common.utils.FomartTimeUtil;
import com.bpic.common.utils.StringUtils;
import com.bpic.common.utils.id.IdGenerator;
import com.bpic.common.utils.json.JsonUtil;
import com.bpic.system.domain.Attachment;
import com.bpic.system.mapper.SysLoginUserMapper;
import com.fulan.api.personnel.domain.*;
import com.fulan.api.personnel.vo.*;

import com.fulan.application.mapper.*;

import com.fulan.application.service.AppPersonnelService;

import com.fulan.application.util.EmployeeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("AppPersonnelServiceImpl")
public class AppPersonnelServiceImpl implements AppPersonnelService {

	@Autowired
	AppPersonnelMapper appPersonnelMapper;
	
	@Autowired
	FamilyMemberMapper familyMemberMapper;
	
	@Autowired
	EducationalMapper educationalMapper;
	
	@Autowired
	WorkExperienceMapper workExperienceMapper;
	
	@Autowired
    InterviewActionMapper interviewActionMapper;
	
	@Autowired
	PersonnelInformInfoMapper personnelInformInfoMapper;
	
	@Autowired
	PersonnelInformMapper personnelInformMapper;

//	@Autowired
//	VcardMessageService vcardMessageService;

	@Autowired
	IdGenerator idGenerator;
	
	@Autowired
	EmployeeUtil employeeUtil;
	
//	@Autowired
//	DictionaryService dictionaryService;
	
//	@Autowired
//    CustomerService customerService;
	
	@Autowired
	PersonnelMapper personnelMapper;
	
//	@Autowired
//	SmsBusiness smsBusinessService;
//
//	@Autowired
//	ContractService contractService;
	
//	@Autowired
//	AttachmentService attachmentService;
//
//	@Autowired
//	private PushService pushService;

	@Value("${erCoreFilePath:\\Doc\\Employee\\File\\Er\\:1}")
	private String path;

	//	签约后附件回传
	@Value("${personnel.attachment.task.url:1}")
	private String url2;

	Logger logger= LoggerFactory.getLogger(AppPersonnelServiceImpl.class);

	@Override
	public int getAgentPersonnelCount(String id) {
		return appPersonnelMapper.getAgentPersonnelCount(id);
	}
	
	@Override
	public int getAgentPersonnelCountByViewer(String id) {
		return appPersonnelMapper.getAgentPersonnelCountByViewer(id);
	}
	/**
	 * 代理人人才库 accountId 当前登录用户id
	 */
	@Override
	public List<AgentPersonnelInfo> getAgentPersonnel(Page<AgentPersonnelInfo> page, FilterVo filterVo) {
		//刷选的时间精确到时分秒
		filterVo = handleFilter(filterVo);
		List<AgentPersonnelInfo> personnelList = appPersonnelMapper.getAgentPersonnel(page, filterVo);
		return personnelList;
	}

	//处理筛选时间
	private FilterVo handleFilter(FilterVo filterVo) {
		Date startTime = filterVo.getStartTime();
		Date endTime = filterVo.getEndTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//处理开始时间
		if(startTime != null){
			String format = dateFormat.format(startTime);
			String subDate = format.substring(0, 10);
			String beginTime = subDate + " 00:00:00";
			try {
				startTime = dateFormat.parse(beginTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			filterVo.setStartTime(startTime);
		}
		//处理结束时间
		if(endTime != null){
			String format1 = dateFormat.format(endTime);
			String subDate1 = format1.substring(0, 10);
			String endTime1 = subDate1 + " 23:59:59";
			try {
				endTime = dateFormat.parse(endTime1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			filterVo.setEndTime(endTime);
		}
		return filterVo;
	}

	/**
	 * 当前查询条件 
	 */
	@Override
	public List<AgentPersonnelInfo> getAgentPersonnelByViewer(Page<AgentPersonnelInfo> page, FilterVo filterVo) {
		//刷选的时间精确到时分秒
		filterVo = handleFilter(filterVo);
		List<AgentPersonnelInfo> personnelList =
				appPersonnelMapper.getAgentPersonnelByViewer(page, filterVo);
		return personnelList;
	}
	
	@Override
	public List<AgentPersonnelInfo> getApprovedAgentPersonnelByViewer(Page<AgentPersonnelInfo> page, FilterVo filterVo) {
		//刷选的时间精确到时分秒
		filterVo = handleFilter(filterVo);
		List<AgentPersonnelInfo> personnelList =
				appPersonnelMapper.getApprovedAgentPersonnelByViewer(page, filterVo);
		return personnelList;
	}
	
	@Override
	public AgentPersonnelInfo getPersonnelInfoForViewer(String personnelId) throws Exception {
		AgentPersonnelInfo agentPersonnelInfo= appPersonnelMapper.getPersonnelInfoForViewer(personnelId);
		if(agentPersonnelInfo.getCreateUser()!=null&&!"".equals(agentPersonnelInfo.getCreateUser())){
			EmployeeVo employeeVo = null;//todo获取代理人信息customerService.queryEmployeeByPresentCode(agentPersonnelInfo.getCreateUser());
			agentPersonnelInfo.setCreateUserName("无");
			if(employeeVo!=null){
				agentPersonnelInfo.setCreateUserName(employeeVo.getEmployeeName());
			}
		}
		//获取工作经历
		List<WorkExperience> list = workExperienceMapper.selectByPersonnelId(Long.valueOf(personnelId));
		//筛选工作经历
		//工作经历
		List<WorkExperience> workExperienceList = new ArrayList<>();
		//同业经历
		List<WorkExperience> insuranceWorkExperienceList = new ArrayList<>();
		for (WorkExperience workExperience :
				list) {
			if(workExperience.getIsInsuranceWork() .equals("1")){
				insuranceWorkExperienceList.add(workExperience);
			}
			if(workExperience.getIsInsuranceWork() .equals("0")){
				workExperienceList.add(workExperience);
			}
		}
		agentPersonnelInfo.setWorkExperienceList(workExperienceList);
		agentPersonnelInfo.setInsuranceWorkExperience(insuranceWorkExperienceList);

		//获取学历信息
		List<Educational> educationalList = educationalMapper.selectByPersonnelId(Long.valueOf(personnelId));
		agentPersonnelInfo.setEducationalList(educationalList);

		return agentPersonnelInfo;
	}

//	@Override
//	@Transactional
//	public Response<List<Attachment>> signedContract(@RequestBody IntegrationVO integrationVO) {
//		logger.info("-------------------====================执行signedContract方法开始"+integrationVO);
//		ResponseCode responseCode = contractService.integrationCA(integrationVO);
//		logger.info("-------------------"+"执行Ca整合接口返回结果:"+responseCode);
//		if("0".equals(responseCode.getCode())){
//			ContractUpload contractUpload = responseCode.getContractUpload();
//			Attachment att = new Attachment();
//			att.setDescription(responseCode.getContractUpload().getAttachment().getDescription());
//
//
//			att.setHostId(integrationVO.getPersonnelId());
//			att.setCategory(integrationVO.getCategory());
//			att.setOriginalName(integrationVO.getCreateByTemplateVO().getTitle() + ".pdf");
//			contractUpload.setAttachment(att);
//			Response<List<Attachment>> uploadContract = attachmentService.uploadContract(contractUpload);
//			return uploadContract;
//		}
//
//		return new Response<>(Response.ERROR, "合同签署失败");
//	}
@Autowired
private SysLoginUserMapper sysLoginUserMapper;
	@Override
	@Transactional
	public void savePersonnelInfo(EnterVo enterVo)  {
		InterviewAction interviewAction = new InterviewAction();
		interviewAction.setPersonnelId(Long.valueOf(enterVo.getPersonnelId()));
		interviewAction.setFlowItemId(Long.valueOf(enterVo.getCurrentStatus()+1));
		//——查询此人是否已经审核完毕，审核完成当前步骤则直接跳出方法
		List<InterviewAction> list = interviewActionMapper.selectByPersonnelIdAndFlowItemId(interviewAction);
		if(list!=null && list.size()>0){
			return ;
		}
		if(enterVo.getCurrentStatus()==3){
			enterVo.setCurrentStatus(2);
			appPersonnelMapper.updateInterviewInfo(enterVo);
			enterVo.setCurrentStatus(3);
		}else {
			appPersonnelMapper.updateInterviewInfo(enterVo);	//更新面试或者流程审核信息
		}

		
		Personnel personnel = personnelMapper.selectById(enterVo.getPersonnelId());
		if(personnel.getCreateUser()==null){
			personnel.setCreateUser(personnel.getSource());
		}
		SysLoginUser createEmployee = null ;
		if(personnel.getCreateUser()!=null){
			createEmployee = sysLoginUserMapper.selectUserByAgentCode(personnel.getCreateUser());
		}



		if ("1".equals(enterVo.getResult())) {	//通过   
			enterVo.setNextStatus(enterVo.getCurrentStatus()+1);
			/***** 添加下一流程节点的初始数据	 *****/
			
			if (enterVo.getNextStatus() == 3) {
				//List<Dictionary> dictList = dictionaryService.findByCodes("rank").getData().get("rank");
				String post = personnel.getConfirmPosition();
				if(post==null){
					post = personnel.getProtocolPosition();
				}
				
//				EmployeeVo employeeVo = employeeUtil.getSecondTrialEmployee(createEmployee, post, dictList);
				
				 //短信发送给复审官

				 //短信发送给推荐人

				//短信发送给被面试人

				enterVo.setNextProName(createEmployee.getC_emp_cnm());
				enterVo.setNextProPerson(createEmployee.getC_emp_cde2());
			} else if(enterVo.getNextStatus() == 4) {
				 //发送给推荐人

				 
				 //发送给被面试人

				
				/*enterVo.setNextProName("zj");
				enterVo.setNextProPerson("953555301552883456");
				enterVo.setNextStatus(6);*/
			} else if(enterVo.getNextStatus() == 5) {
				enterVo.setNextProName("zj");
				enterVo.setNextProPerson("953555301552883456");
			} else {
				enterVo.setNextProName("负责人" + enterVo.getNextStatus());	//插入负责人姓名
				enterVo.setNextProPerson(enterVo.getNextStatus() + "0001");	//插入负责人Id
			}
			enterVo.setTagId(idGenerator.generate());
			appPersonnelMapper.insertInterviceInfo(enterVo);	//插入一条新的流程信息,进入下一流程
		} else {
			 //发送给推荐人
			 enterVo.setNextStatus(enterVo.getCurrentStatus()+1);

			 
			 //发送给面试人

			/***** 更新人才当前所在流程信息	 *****/
			enterVo.setNextStatus(-1);
		}
		appPersonnelMapper.updatePersonnelInfo(enterVo);	//更新主表人才流程信息(下一流程,职级调整)
	}
	
	@Override
	public PersonnelBaseVo getPersonnelBaseInfo(String personnelId) throws Exception {
		//个人信息
		PersonnelBaseVo personnelBaseVo = new PersonnelBaseVo();
		personnelBaseVo.setPersonnel(appPersonnelMapper.getPersonnelBaseInfo(personnelId));
		
		//家庭信息
		FamilyMember searchFamilyMember = new FamilyMember();
		searchFamilyMember.setPersonnelId(Long.valueOf(personnelId));
		searchFamilyMember.setMemberType("1");
		personnelBaseVo.setFamilyMemberList(familyMemberMapper.selectByPersonnelId(searchFamilyMember));
		
		//教育经历
		personnelBaseVo.setEducationalList(educationalMapper.selectByPersonnelId(Long.valueOf(personnelId)));
		List<WorkExperience> list = workExperienceMapper.selectByPersonnelId(Long.valueOf(personnelId));
		List<WorkExperience> InsuranceWorkExperiencelist = new ArrayList<>();
		List<WorkExperience> WorkExperiencelist = new ArrayList<>();
		if(list != null && list.size() > 0){
			for (WorkExperience workExperience:
					list) {
				if(workExperience.getIsInsuranceWork().equals("1")){
					InsuranceWorkExperiencelist.add(workExperience);
				}
				if(workExperience.getIsInsuranceWork().equals("0")){
					if(workExperience.getPost() != null && !"".equals(workExperience.getPost())){
						WorkExperiencelist.add(workExperience);
					}
				}
			}
		}
		//工作经历
	    personnelBaseVo.setWorkExperienceList(WorkExperiencelist);
		//同业经历
		personnelBaseVo.setInsuranceWorkExperience(InsuranceWorkExperiencelist);
		return personnelBaseVo;
	}

	@Override
	@Transactional
	public void updatePersonnelBaseInfo(PersonnelBaseVo personnelBaseVo) throws Exception {
		if(personnelBaseVo.getPersonnel()!=null){
		    Long personnelId = personnelBaseVo.getPersonnel().getId();
			familyMemberMapper.deleteByPersonnelId(personnelId);
			educationalMapper.deleteByPersonnelId(personnelId);
			workExperienceMapper.deleteByPersonnelId(personnelId);
			appPersonnelMapper.updatePersonnelBaseInfo(personnelBaseVo.getPersonnel());	//更新主表人才信息(下一流程,职级调整)
		
		    if(personnelBaseVo.getFamilyMemberList()!=null &&personnelBaseVo.getFamilyMemberList().size()>0){
				for(FamilyMember familyMember : personnelBaseVo.getFamilyMemberList()){
					familyMember.setMemberType("1");
					familyMember.setId(idGenerator.generate());
					familyMember.setPersonnelId(personnelId);
					familyMemberMapper.insertSelective(familyMember);
				}
			}
			
			if(personnelBaseVo.getEducationalList()!=null && personnelBaseVo.getEducationalList().size()>0){
				for(Educational educational :  personnelBaseVo.getEducationalList()){
					educational.setPersonnelId(personnelId);
					educational.setId(idGenerator.generate());
					educationalMapper.insertSelective(educational);
				}
			}
			
			if(personnelBaseVo.getWorkExperienceList()!=null && personnelBaseVo.getWorkExperienceList().size()>0){
				for(WorkExperience workExperience :  personnelBaseVo.getWorkExperienceList()){
					workExperience.setPersonnelId(personnelId);
					workExperience.setId(idGenerator.generate());
					workExperienceMapper.insertSelective(workExperience);
				}
			}
		}
		
		
		
	}

	@Override
	@Transactional
	public void updatePersonnel(Personnel updatePersonnel) throws Exception {
		//获取人员信息
		Personnel personnel = personnelMapper.selectByPersonnelId(Long.valueOf(updatePersonnel.getId()));
		//更新核心的银行卡数据信息
		if(!StringUtils.isEmpty(personnel.getAgentCode())&&(!StringUtils.isEmpty(personnel.getBankBranch()) || !StringUtils.isEmpty(updatePersonnel.getBankBranch()))){
			Map<String,Object> columnMap = new HashMap<String,Object>(); 
			columnMap.put("bank_branch", updatePersonnel.getBankBranch());
			columnMap.put("china_citic_bank", updatePersonnel.getChinaCiticBank());
			columnMap.put("bank_name", updatePersonnel.getBankName());
			List<Personnel> list = personnelMapper.selectByMap(columnMap);
			if(null==list||list.isEmpty()){
				//获取推荐人信息
				EmployeeVo searchEmployeeNew = new  EmployeeVo();
				InterviewAction searchInterviewAction = new InterviewAction();
				searchInterviewAction.setPersonnelId(personnel.getId());
				//——!!内勤时无面试
				InterviewAction reInterviewAction = interviewActionMapper.getFirstInterviewAction(searchInterviewAction);
				searchEmployeeNew.setPresentCode(reInterviewAction.getProcessingPerson());
				List<HashMap<String, Object>>  employeeVoList = null;//todo customerService.queryListNew(searchEmployeeNew);
				Map<String,Object> employeeVo = null;
				if(employeeVoList!=null&&employeeVoList.size()>0){
					employeeVo = employeeVoList.get(0);
				}
				
				searchEmployeeNew.setPresentCode(personnel.getAgentCode());
				employeeVoList =null; //todo customerService.queryListNew(searchEmployeeNew);
				String EmployeeID = null;
				if(employeeVoList!=null&&employeeVoList.size()>0){
					EmployeeID = String.valueOf(employeeVoList.get(0).get("EmployeeID")) ;
				}
				/*
				* TODO 更新用户信息
				* */
//				EmployeeNewVo employeeNewVo = new EmployeeNewVo();
//				//紧急联系人
//				List<FamilyMember> famList = familyMemberMapper.selecPersonnelById(personnel.getId());
//				FamilyMember familyMember = null;
//				if(famList!=null&&famList.size()>0){
//					familyMember = famList.get(0);
//				}
//				employeeNewVo.setEmployeeNew(saveEmployeeNew(personnel, employeeVo,familyMember,EmployeeID));
//				//银行附件信息
//				List<CertificateModel> certificateModels = new ArrayList<>();
//				if(updatePersonnel.getBankBranch()!=null){
//					CertificateModel certificateModel = new CertificateModel();
//					certificateModel.setID(17);
//					certificateModel.setCertificateID(17);
//					certificateModel.setCertificateNo(updatePersonnel.getBankBranch());
//					certificateModel.setEmployeeID(EmployeeID);
//					certificateModel.setCertificateMemo(DateUtil.toSeconds(new Date()));
//					certificateModels.add(certificateModel);
//				}
//				if(updatePersonnel.getChinaCiticBank()!=null){
//					CertificateModel certificateModel = new CertificateModel();
//					certificateModel.setID(1);
//					certificateModel.setCertificateID(1);
//					certificateModel.setCertificateNo(updatePersonnel.getChinaCiticBank());
//					certificateModel.setEmployeeID(EmployeeID);
//					certificateModel.setCertificateMemo(DateUtil.toSeconds(new Date()));
//					certificateModels.add(certificateModel);
//				}
//				EmployeeNewListVo employeeNewListVo = new EmployeeNewListVo();
//				employeeNewListVo.setCertificateModel(certificateModels);
//				employeeNewVo.setEmployeeNewListVo(employeeNewListVo);
//				customerService.updateEmployeeNew(employeeNewVo);
			}
//			同步银行卡信息
			try {
//				Personnel personnel1 = personnelMapper.selectByPersonnelId(updatePersonnel.getId());
//				Map<String,String> response1 = attachmentService.erCopyFile("42",""+updatePersonnel.getId());
//				List list1 = new ArrayList<>();
//				list1.add(response1.get("42"));
//				new PersonnelAttachmentTask(url2,path,"CertificateAddResult",""+DateUtil.toSeconds(new Date()),"0",
//						"1","0",personnel1.getAgentCode(),updatePersonnel.getChinaCiticBank(),list1
//				).run();
			}catch (Exception e){
				logger.error("同步银行卡信息失败"+e);
			}
		}
		appPersonnelMapper.updatePersonnelBaseInfo(updatePersonnel);
	}


	@Override
	@Transactional
	public void updatePersonnelInform(PersonnelInformVo personnelInformVo) throws Exception {
		if(personnelInformVo.getPersonnelInform()!=null&&personnelInformVo.getPersonnelInform().getPersonnelId()!=null){
			Long personnelId = personnelInformVo.getPersonnelInform().getPersonnelId();
			Map<String, Object> delMap = new HashMap<String,Object>();
			delMap.put("personnel_id", personnelId);
			personnelInformMapper.deleteByMap(delMap);
			personnelInformInfoMapper.deleteByMap(delMap);
			
			PersonnelInform personnelInform = personnelInformVo.getPersonnelInform();
			Long personnelInformId = idGenerator.generate();
			personnelInform.setId(personnelInformId);
			personnelInform.setPersonnelId(personnelId);
			personnelInformMapper.insert(personnelInform);
			
			if( personnelInformVo.getPersonnelInformInfoList()!=null && personnelInformVo.getPersonnelInformInfoList().size()>0){
				for(PersonnelInformInfo personnelInformInfo:personnelInformVo.getPersonnelInformInfoList()){
					personnelInformInfo.setInformType("1");
					personnelInformInfo.setPersonnelId(personnelId);
					personnelInformInfo.setId(idGenerator.generate());
					personnelInformInfo.setPersonnelInformId(personnelInformId);
					personnelInformInfoMapper.insertSelective(personnelInformInfo);
				}
			}
			
		}
		
	}

	@Override
	public PersonnelInformVo getInform(Personnel personnel) {
		Map<String,Object> searchMap = new HashMap<String ,Object>();
		searchMap.put("personnelId", personnel.getId());
		return personnelInformInfoMapper.getInform(searchMap);
	}

	@Override
	public Personnel getPersonnelByPresonnel(Personnel personnel) {
		Map<String,Object> searchMap = new HashMap<String,Object>();
		if(personnel.getCellphone()!=null){
			searchMap.put("cellphone", personnel.getCellphone());
		}
		List<Personnel> reList = personnelMapper.selectByMap(searchMap);
		if(reList!=null&&reList.size()>0){
			personnel = reList.get(0);
		}
		return personnel;
	}

	@Override
	@Transactional
	public Boolean clearBankInfo(Personnel personnel) {
		if(personnel != null && personnel.getId() != null){
			appPersonnelMapper.clearBankInfo(personnel);
			List<Attachment> attachmentList = null;//todo attachmentService.findbyparms(42,personnel.getId());
			if(attachmentList!=null){
				for (Attachment attachment:
					 attachmentList) {
					attachment.setIsDelete(0);
					//todo attachmentService.updateById(attachment);
				}
			}
		}else{
			return false;
		}
		return true;
	}
	
	/**
	 * 获取设置过参数的EmployeeNew
	 * @param personnel
	 * @param employeeVo
	 * @return EmployeeNew
	 */
	public EmployeeNew saveEmployeeNew(Personnel personnel, Map<String,Object> employeeVo, FamilyMember familyMember, String EmployeeID){
		EmployeeNew employeeNew = new EmployeeNew();
		employeeNew.setEmployeeID(EmployeeID);
		//出生日期
		employeeNew.setBirthday(FomartTimeUtil.ymdUtil(personnel.getBirthday()));
		//分支机构ID
		employeeNew.setBranchID(personnel.getOrgId());
		//证件代码
		employeeNew.setCertificateCode(personnel.getIdentityCode());
		//证件类型
		employeeNew.setCertificateType(EmployeeUtil.getIdCardCode(personnel.getIdentityType()));
		//签约开始时间
		employeeNew.setContractBeginDate(FomartTimeUtil.ymdUtil(new Date()));
		//签约结束时间
		employeeNew.setContractEndDate(0);
		//创建日期
		employeeNew.setCreatedDate(FomartTimeUtil.ymdhmsUtil(new Date()));
		//创档人ID
		employeeNew.setCreator(personnel.getCreateUser());
		//目前地址
		employeeNew.setCurrentAddress(personnel.getFamilyAddrDetail());
		//当前电话
		employeeNew.setCurrentPhone(personnel.getCellphone());
		//目前所在地邮政编码
		employeeNew.setCurrentZip(personnel.getPostcode());
		//部门ID
		employeeNew.setDepartmentID(String.valueOf(employeeVo.get("DepartmentID")));
		//学历类型(0：小学1：初中2：高中3：专科4：本科5：硕士6：博士7：博士后 254：其他)
		if(personnel.getEducation()!=null){
			employeeNew.setEducationType(Byte.valueOf(personnel.getEducation()));
		}
		//邮箱
		employeeNew.setEmail(personnel.getEmail());
		//员工名称
		employeeNew.setEmployeeName(personnel.getName());
		//家庭地址
		employeeNew.setFamilyAddress(personnel.getDomicilePlace());
		//家庭电话
		employeeNew.setFamilyPhone(personnel.getTelephone());
		//介绍人Id
		Map<String,Object> jsEmployVo = new HashMap<String,Object>();
		if(personnel.getCreateUser()!=null&&!"".equals(personnel.getCreateUser())){
			EmployeeNew searchEmployeeNew = new  EmployeeNew();
			searchEmployeeNew.setPresentCode(personnel.getCreateUser());
			List<HashMap<String, Object>>  employeeVoList = null;//todo customerService.queryListNew(searchEmployeeNew);
			
			if(employeeVoList!=null&&employeeVoList.size()>0){
				jsEmployVo = employeeVoList.get(0);
			}
		}
		employeeNew.setIntroducerID(String.valueOf(jsEmployVo.get("EmployeeID")));
		//是否可用
		employeeNew.setIsDeleted(0);
		//最後修改日期(日期格式-YYYYMMDDHHMMSS）
		employeeNew.setLastUpdateDate(FomartTimeUtil.ymdhmsUtil(new Date()));
		//婚姻状况（0、未婚；1、已婚）
		//employeeNew.setMarried((byte)0);
		//备注
		//employeeNew.setMemo(null);
		//更新档人ID
		//employeeNew.setMender(null);
		//手机号码
		employeeNew.setMobilePhone(personnel.getCellphone());
		//民族ID
//		Dictionary dict = dictionaryService.findByCode(personnel.getNation());
//		if(dict!=null){
//			employeeNew.setNationID(Integer.valueOf(dict.getValue()));
//		}
		//籍贯
		//employeeNew.setNative();
		//入司日期
		employeeNew.setOnboardDate(FomartTimeUtil.ymdUtil(new Date()));
		//户籍地址
		employeeNew.setParentFamilyAddress(personnel.getDomicilePlace());
		//相片地址
		//employeeNew.setPhotoPath("");
		//先前修改时间
		//employeeNew.setPrecedUpateDate(0L);
		//员工码
		employeeNew.setPresentCode(personnel.getAgentCode());
		//性别
		if("M".equals(personnel.getSex())){
			employeeNew.setSex(0);
		}else{
			employeeNew.setSex(1);
		}
		
		//紧急联络人地址
		// employeeNew.setSOSAddress(null);
		if(familyMember!=null){
			//紧急联络人姓名
			employeeNew.setSOSName(familyMember.getName());
			//紧急联络人电话
			employeeNew.setSOSPhone(familyMember.getTelephone());
		}
		//员工类型（0、内勤；1、外勤）
		employeeNew.setType((byte)1);
		//验证状态（0、解锁状态；1、上锁状态）
		employeeNew.setValidate(0);
		//验证日期
		//employeeNew.setValidateDate(0);
		//体重
		//employeeNew.setWeight(0);
		//合同号
		List<Attachment> list = null;//attachmentService.findbyparms(147, personnel.getId());
		if(list!=null&&list.size()>0){
			employeeNew.setContractNo(list.get(0).getDescription());
		}
		return employeeNew;
	}

//	检查当前人是否可以更新
	@Override
	public Boolean checkCanUpdateByPersonnelId(String personnelId) {
		Personnel personnel = personnelMapper.selectById(personnelId);
		if(personnel != null){
//			非关闭状态且未完成
			if((personnel.getIsClose() == null ||
					(personnel.getIsClose()!=null && personnel.getIsClose()!=1))
					&& StringUtils.isEmpty(personnel.getAgentCode())){
				logger.info("更新检查结果："+true);
				return true;
			}else{
				logger.info("更新检查结果："+false);
				return false;
			}
		}else{
			logger.info("更新检查结果："+false);
			return false;
		}
	}
}
