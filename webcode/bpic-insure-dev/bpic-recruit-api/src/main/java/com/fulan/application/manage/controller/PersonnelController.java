package com.fulan.application.manage.controller;

import com.bpic.common.constant.HttpStatus;
import com.bpic.common.core.controller.BaseController;
import com.bpic.common.core.domain.model.LoginUser;
import com.bpic.common.core.page.TableDataInfo;
import com.bpic.common.utils.StringUtils;
import com.bpic.common.utils.json.JsonUtil;
import com.bpic.common.utils.reponse.Response;
import com.bpic.framework.web.service.TokenService;
import com.bpic.sms.SendSmsUtil;
import com.fulan.api.personnel.domain.Check;
import com.fulan.api.personnel.domain.Personnel;
import com.fulan.api.personnel.domain.ShareInfo;
import com.fulan.api.personnel.vo.*;
import com.fulan.application.service.*;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value ="/personnel/manage/personnel")
public class PersonnelController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(PersonnelController.class);

	@Resource(name="PersonnelServiceImpl")
	PersonnelService personnelService;

	@Resource(name = "FamilyMemberServiceImpl")
	private FamilyMemberService familyMemberService;

	@Resource(name = "WorkExperienceServiceImpl")
	private WorkExperienceService workExperienceService;

	@Resource(name = "EducationalServiceImpl")
	private EducationalService educationalService;

	@Resource(name = "ShareInfoServiceImpl")
	private ShareInfoService shareInfoService;

	@Autowired
	TokenService tokenService;

	@Autowired
	SendSmsUtil sendSmsUtil;



	/**
	 * 查询面试官
	 * @param
	 * @return
	 */
//	updateSpecific()	指定某个人面试
//	insertSpecific()	指定流程
	@ApiOperation(value = "指派面试官-查询面试官", notes = "指派面试官-查询面试官", response = Response.class)
	@RequestMapping(value = "/selectSpecific", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<List<SpecificVo>> selectSpecific(@RequestParam(value = "code",required = false) String  code,
													 @RequestParam(value = "name",required = false) String  name){
		Response<List<SpecificVo>> response = null;
		try {
			EmployeeNew employeeNew=new EmployeeNew();
			employeeNew.setPresentCode(code);
			employeeNew.setEmployeeName(name);
			List<HashMap<String, Object>> queryListNew = null;//customerService.queryListNew(employeeNew);
			List<SpecificVo> employ=new ArrayList<>();
			for (HashMap<String, Object> hashMap : queryListNew) {
				SpecificVo em=new SpecificVo();
				if(hashMap != null){
					em.setPresentCode(hashMap.get("PresentCode")!=null?hashMap.get("PresentCode").toString():"");
					em.setName(hashMap.get("EmployeeName")!=null?hashMap.get("EmployeeName").toString():"");
					em.setSex(hashMap.get("SexName")!=null?hashMap.get("SexName").toString():"");
					em.setCertificateCode(hashMap.get("CertificateCode")!=null?hashMap.get("CertificateCode").toString():"");
					em.setDutyName(hashMap.get("DutyName")!=null?hashMap.get("DutyName").toString():"");
					em.setBranch(hashMap.get("BranchName")!=null?hashMap.get("BranchName").toString():"");
					em.setPhone(hashMap.get("MobilePhone")!=null?hashMap.get("MobilePhone").toString():"");
					em.setSource(hashMap.get("PresentCode")!=null?hashMap.get("PresentCode").toString():"");
					em.setOrgId(hashMap.get("BranchID")!=null?hashMap.get("BranchID").toString():"");
					employ.add(em);
				}
			}
			response = new Response<>(Response.SUCCESS,Response.SUCCESS_MESSAGE);
			response.setData(employ);
		}catch (Exception e){
			response = new Response<>(Response.ERROR,Response.ERROR_MESSAGE);
			logger.error(""+e);
		}
		return response;
	}


	@PostMapping("/updateSpecific")
	@ResponseBody
	public Response<String> updateSpecific(@RequestBody InterviewActionVo interviewActionVo){
		Response<String> resp=personnelService.updateSpecific(interviewActionVo);
		return resp;
	}


	@ApiOperation(value = "获取个人审核图片信息", notes = "获取个人审核图片信息", response = Response.class)
	@RequestMapping(value = "/getImgCheck", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<Check> getImgCheck(@RequestParam(value = "personnelId",required = true) String personnelId){
		logger.info("getImgCheck入参："+personnelId);
		Response response = null;
		try {
			Check check = personnelService.getImgCheck(personnelId);
			response =  new Response(Response.SUCCESS,Response.SUCCESS_MESSAGE);
			response.setData(check);
		}catch (Exception e){
			logger.error(""+e);
			response = new Response(Response.ERROR,Response.ERROR_MESSAGE);
		}
		return response;
	}



//	@ApiOperation(value = "获取个人流程信息", notes = "获取个人面试流程信息", response = Response.class)
//	@RequestMapping(value = "/personnelFlow", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
//	public Response<PersonnelFlowVo> personnelFlow(@RequestParam(value = "personnelId",required = true) String personnelId){
//		logger.info("personnelFlow入参："+personnelId);
//		Response<PersonnelFlowVo> response = null;
//		try {
//			PersonnelFlowVo personnelFlowVo = personnelService.personnelFlow(personnelId);
//			response = new Response<PersonnelFlowVo>(Response.SUCCESS,Response.SUCCESS_MESSAGE);
//			response.setData(personnelFlowVo);
//		}catch (Exception e){
//			response = new Response<>(Response.ERROR,Response.ERROR_MESSAGE);
//			logger.error(""+e);
//		}
//		return response;
//	}

	@PostMapping("/insertSpecific")
	@ResponseBody
	public Response<String> insertSpecific(@RequestBody InterviewActionVo interviewActionVo){
		Response<String> resp= null;
		try {
			resp = personnelService.insertSpecificNew(interviewActionVo);
		}catch (Exception e){
			logger.info("insertSpecific出现异常:",e);
			resp = new Response<>(Response.ERROR,Response.ERROR_MESSAGE);
		}
		return resp;
	}

	/**
	 * 培训审核处理
	 * @param
	 * @param
	 * @param
	 * @return
	 */
	@PostMapping("/checkTrainPersonnel")
	@ResponseBody
	public Response<String> checkTrainPersonnel(
			@RequestParam(value = "personnelId",required = true) String id ,
			@RequestParam(value = "status",required = true) String status,
			@RequestParam(value = "nowItem",required = true) String nowItem,
			@RequestParam(value = "result",required = true) String result){
		logger.info("id:"+id+"_status:"+status+"_nowItem:"+nowItem+"_result"+result);
		try {
			if("1".equals(status)){
				InterviewActionVo interviewActionVo =new InterviewActionVo();
				interviewActionVo.setPersonnelId(id);
				interviewActionVo.setFlowItemId("6");
				interviewActionVo.setNowItem(nowItem);
				interviewActionVo.setProcessingDesc(result);
				Response<String> resp=personnelService.insertSpecific(interviewActionVo);
				return resp;
			}else if("0".equals(status)){
				Response<String> res = personnelService.updatePersonnerStaus(id,"-1");
				Response<String> resp= personnelService.updateInterviewActionByIdCode(id,nowItem,"2");
				return res;
			}
		}catch (Exception e){
			logger.error("审核异常！");
		}
		return new Response<>(Response.ERROR,Response.ERROR_MESSAGE);
	}

	@ApiOperation(value = "获取列表信息", notes = "获取列表信息", response = Response.class)
	@PostMapping("/personnelList")
	@ResponseBody
	public TableDataInfo personnelList(@RequestBody PersonnelSearchVo personnelSearchVo) throws Exception{
		logger.info("personnelList入参："+ JsonUtil.getJSONString(personnelSearchVo));
		try {
			startPage();
			List<PersonnelResultVo> page = personnelService.personnelList(personnelSearchVo);
			return getDataTable(page);
		}catch (Exception e){
			logger.error(""+e);
			TableDataInfo tableDataInfo = new TableDataInfo();
			tableDataInfo.setCode(HttpStatus.ERROR);
			tableDataInfo.setMsg("系统错误");
			return tableDataInfo;
		}
	}



	/**
	 * @Author Mr.chen
	 * @Description
	 * //TODO
	 * @Date
	 * @Param
	 * @return
	 **/
	@ApiOperation(value = "签约转发", notes = "签约转发", response = Response.class)
	@PostMapping(value = "/sendSignMessage")
	public Response<String> sendSignMessage(@RequestParam(value = "personnelId",required = true) String personnelId){
		logger.info("personnelId："+personnelId);
		Response<String>  res = new Response<>(Response.SUCCESS,Response.SUCCESS_MESSAGE);
		String phone = null;
		try {
			PersonnelFlowItemVo personnelFlowItemVo=personnelService.selectPersonner(personnelId);
			String persennelStatus = personnelFlowItemVo.getPersonnelStatus();
			if(!"6".equals(persennelStatus)){
				res = new Response<>(Response.ERROR,Response.ERROR_MESSAGE);
				res.setData("不在签约转发流程!!");
				return res;
			}
			phone =  personnelFlowItemVo.getCellphone();
			String url="http://mosales.bpic.com.cn";
			res = personnelService.updateBatchPersonnelisSend(personnelId);
			if(res.getCode()==200){
				String position = personnelFlowItemVo.getProtocolPosition();
				if(personnelFlowItemVo.getConfirmPosition()!=null&&!"".equals(personnelFlowItemVo.getConfirmPosition())){
					position = personnelFlowItemVo.getConfirmPosition();
				}
				//发送短信
				String url2 = url+"/web/home/#page=baseInfo&personnelId="+personnelId;
				saveLink(url2);
				StringBuffer sb = new StringBuffer();
				sb.append("恭喜您通过培训，请在七日内填写资料进行签约。应聘职位【")
				.append(position)
				.append("】")
				.append("签约链接【").append(url2).append("】");
				sendSmsUtil.sendSms(phone, sb.toString());
				res.setMsg("转发成功");
			}
			return res;
		}catch (Exception e){
			logger.error("sendSignMessage:"+e);
			res = new Response<>(Response.ERROR,Response.ERROR_MESSAGE);
		}
		return res;
	}


	/**
	 * 人才资料审核结果
	 * @return
	 */
	@PostMapping("/checkSignResult")
	@ResponseBody
	@Transactional
	public Response<String> checkSignResult(
			@RequestBody Check check){
		try {
			Personnel personnel = personnelService.selectPersonnerById(check.getPersonnelId());
			if("1".equals(personnel.getCheckResult()) || "2".equals(personnel.getCheckResult())) {
				Response<String> resp = new Response<String>(Response.SUCCESS, "更新成功");
				resp.setData(null);
				return resp;
			}
			LoginUser currentUser =  tokenService.getLoginUser();
			Response<String> res = personnelService.checkDataResult(check.getPersonnelId(),check.getCheckOpinion(),""+check.getCheckResult(),currentUser.getUser().getC_emp_cnm(),currentUser.getUser().getC_emp_cde());
			return res;
		}catch (Exception e) {
			logger.error("核查失败", e);
			return new Response<>(Response.ERROR, Response.ERROR_MESSAGE);
		}

	}


	public String saveLink(String url){
		logger.info("saveLink入参"+url);
		LoginUser  loginUser= tokenService.getLoginUser();

		ShareInfo shareInfo = new ShareInfo();
		shareInfo.setShareUrl(url);
		if(StringUtils.isNotNull(loginUser)){
			shareInfo.setCreateUser(loginUser.getUser().getC_emp_cde());
		}
		Response<ShareInfo> response = addShare(shareInfo);
		return response.getData().getShareUrl();
	}

	@ApiOperation(value = "链接保存", notes = "链接保存", response = Response.class)
	@RequestMapping(value = "/addShare", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<ShareInfo> addShare(@RequestBody ShareInfo shareInfoVo) {
		try {
			return shareInfoService.addShare(shareInfoVo);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new Response<ShareInfo>(Response.ERROR,"链接保存失败");
		}
	}







	@RequestMapping(value="/exclusions/checkDataResult",method=RequestMethod.POST)
	@ResponseBody
	public  Response<String> checkDataResultFront(@RequestBody Personnel personnel){
		Response<String> res=personnelService.submitPersonnel(personnel);
		return  res;
	}






}
