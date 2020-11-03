package com.fulan.application.custom.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.bpic.common.core.domain.entity.TempCdeSales;
import com.bpic.common.core.domain.model.LoginUser;
import com.bpic.common.utils.ServletUtils;
import com.bpic.common.utils.StringUtils;
import com.bpic.common.utils.reponse.Response;

import com.bpic.framework.web.service.TokenService;
import com.bpic.system.mapper.SysUserMapper;
import com.fulan.api.flow.vo.FlowItemInterviewActionVo;
import com.fulan.api.personnel.domain.InterviewAction;
import com.fulan.api.personnel.domain.Personnel;
import com.fulan.api.personnel.domain.PersonnelInformInfo;
import com.fulan.api.personnel.domain.ShareInfo;
import com.fulan.api.personnel.vo.*;

import com.fulan.application.mapper.WorkExperienceMapper;

import com.fulan.application.service.AppPersonnelService;

import com.fulan.application.service.InterviewActionService;
import com.fulan.application.service.PersonnelService;
import com.fulan.application.service.ShareInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "app-personnel", description = "app在线增员-个人信息接口")
@RestController
@RequestMapping(value ="/personnel/customer/app-personnel")
public class AppPersonnelCustomerController {
	
	private static Logger logger = LoggerFactory.getLogger(AppPersonnelCustomerController.class);
	
	@Resource(name="AppPersonnelServiceImpl")
	AppPersonnelService AppPersonnelService;

	@Resource(name="PersonnelServiceImpl")
	PersonnelService personnelService;

	@Resource(name="InterviewActionServiceImpl")
	InterviewActionService interviewActionService;

	@Resource(name="ShareInfoServiceImpl")
	ShareInfoService shareInfoService;

	@Autowired
	SysUserMapper sysUserMapper;



	@Autowired
	WorkExperienceMapper workExperienceMapper;


	@Autowired
	private TokenService tokenService;


	@ApiOperation(value = "获取用户信息", notes = "录入和面试人才数量", response = Response.class)
	@RequestMapping(value = "/getUserInfo", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<Object> getUserInfo(@RequestParam(name = "agentCode",required = false) String agentCode) {
		Response resp = new Response<Map<String, Object>>(Response.SUCCESS,"查询成功");
		try {
			if(StringUtils.isEmpty(agentCode)){
				agentCode =	tokenService.getLoginUser().getUser().getC_emp_cde();
			}
			Map<String,Object> searchMap = new HashMap<String,Object>();
			searchMap.put("c_emp_cde",agentCode);
			resp.setData(sysUserMapper.selectByMap(searchMap).get(0));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp = new Response<Map<String, Object>>(Response.ERROR,"查询失败");
		}
		return resp;
	}


	@ApiOperation(value = "录入和面试人才数量", notes = "录入和面试人才数量", response = Response.class)
	@RequestMapping(value = "/getAgentPersonnelCount", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<Map<String, Object>> getAgentPersonnelCount(@RequestParam(required = false) String personnelId) {
		Response<Map<String, Object>> resp = 
				new Response<Map<String, Object>>(Response.SUCCESS,"查询成功");
		try {
			LoginUser account = tokenService.getLoginUser();//todo (Account) redisUtil.getUserInfo();// 获取用户对象
			if (account != null) {
				personnelId = String.valueOf(account.getUser().getC_emp_cde());
			}
			int enterCount = AppPersonnelService.getAgentPersonnelCount(personnelId);
			int vieweeCount = AppPersonnelService.getAgentPersonnelCountByViewer(personnelId);
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("enterCount", enterCount);
			result.put("vieweeCount", vieweeCount);
			resp.setData(result);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp = new Response<Map<String, Object>>(Response.ERROR,"查询失败");
		}
		return resp;
	}

	@ApiOperation(value = "获取招募路径", notes = "录入和面试人才数量", response = Response.class)
	@RequestMapping(value = "/findByCode", produces = { "application/json;charset=utf-8" }, method = RequestMethod.GET)
	public Response<String> findByCode(@RequestParam(required = false) String personnelId) {
		Response<String> resp =
				new Response<String>(Response.SUCCESS,"查询成功");
		resp.setData("111111");
		return resp;
	}
	
	/**
	 * 取得面试人才数量
	 */
//	@ApiOperation(value = "本人面试人才数量", notes = "本人面试人才数量", response = Response.class)
//	@RequestMapping(value = "/getAgentPersonnelCountByViewer", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
//	public Response<Integer> getAgentPersonnelCountByViewer(@RequestParam String personnelId) {
//		Response<Integer> resp = new Response<Integer>(Response.SUCCESS,"查询成功");
//		try {
//			resp.setData(AppPersonnelService.getAgentPersonnelCountByViewer(personnelId));
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//			resp = new Response<Integer>(Response.ERROR,"查询失败");
//		}
//		return resp;
//	}

//	/**
//	 * 合同签署上传
//	 * @param response
//	 * @param integrationVO
//	 * @return
//	 */
//	@ApiOperation(value = "合同签署上传", notes = "合同签署上传", response = Response.class)
//	@RequestMapping(value = "/exclusions/signedContract", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
//	public Response<List<Attachment>> signedContract(@RequestBody IntegrationVO integrationVO){
//		return AppPersonnelService.signedContract(integrationVO);
//	}
//
	/**
	 * 查询招募进度列表
	 * @param accountId		当前登入人ID
	 * @param name	人才姓名
	 * @param status	招募状态
	 * @param rank		招募人职级
	 * @return
	 */
	@ApiOperation(value = "本人招募人才查询", notes = "本人招募人才查询", response = Response.class)
	@RequestMapping(value = "/getAgentPersonnel", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<Page<AgentPersonnelInfo>> getAgentPersonnel(
			@RequestBody(required = false) FilterVo filterVo) {
		LoginUser account = tokenService.getLoginUser();

		if (account != null ) {
			filterVo.setAccountId( String.valueOf(account.getUser().getC_emp_cde()));
		}
		Response<Page<AgentPersonnelInfo>> resp =
				new Response<Page<AgentPersonnelInfo>>(Response.SUCCESS,"本人招募人才查询成功");
		Page<AgentPersonnelInfo> page =
				new Page<AgentPersonnelInfo>(filterVo.getPageNo(), filterVo.getPageSize());
		page.setRecords(AppPersonnelService.getAgentPersonnel(page, filterVo));
		resp.setData(page);
		return resp;
	}
	
	/**
	 * 查询招募审批列表
	 * @param accountId		当前登入人ID
	 * @param name	人才姓名
	 * @param status	招募状态
	 * @param rank		招募人职级
	 * @return
	 */
	@ApiOperation(value = "招募面试未审批列表查询", notes = "招募面试未审批列表查询", response = Response.class)
	@RequestMapping(value = "/getAgentPersonnelByViewer", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<Page<AgentPersonnelInfo>> getAgentPersonnelByViewer(
			@RequestBody(required = false) FilterVo filterVo) {
		LoginUser account = tokenService.getLoginUser();// 获取用户对象

		if (account != null ) {
			filterVo.setAccountId( String.valueOf(account.getUser().getC_emp_cde()));
		}
		Response<Page<AgentPersonnelInfo>> resp = null;
		try {
			resp = new Response<Page<AgentPersonnelInfo>>(Response.SUCCESS,"招募面试列表查询成功");
			Page<AgentPersonnelInfo> page = new Page<AgentPersonnelInfo>(filterVo.getPageNo(), filterVo.getPageSize());
			page.setRecords(AppPersonnelService.getAgentPersonnelByViewer(page, filterVo));
			resp.setData(page);
		} catch(Exception e) {
			resp = new Response<Page<AgentPersonnelInfo>>(Response.SUCCESS,"招募面试列表查询失败");
		}
		return resp;
	}	
	
	/**
	 * 查询招募审批列表
	 * @param accountId		当前登入人ID
	 * @param name	人才姓名
	 * @param status	招募状态
	 * @param rank		招募人职级
	 * @return
	 */
	@ApiOperation(value = "招募面试已审批列表查询", notes = "招募面试已审批列表查询", response = Response.class)
	@RequestMapping(value = "/getApprovedAgentPersonnelByViewer", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<Page<AgentPersonnelInfo>> getApprovedAgentPersonnelByViewer(
			@RequestBody(required = false) FilterVo filterVo) {
		LoginUser account = tokenService.getLoginUser();// 获取用户对象

		if (account != null ) {
			filterVo.setAccountId( String.valueOf(account.getUser().getC_emp_cde()));
		}

		Response<Page<AgentPersonnelInfo>> resp = null;
		try {
			resp = new Response<Page<AgentPersonnelInfo>>(Response.SUCCESS,"招募面试列表查询成功");
			Page<AgentPersonnelInfo> page = new Page<AgentPersonnelInfo>(filterVo.getPageNo(), filterVo.getPageSize());
			page.setRecords(AppPersonnelService.getApprovedAgentPersonnelByViewer(page, filterVo));
			resp.setData(page);
		} catch(Exception e) {
			resp = new Response<Page<AgentPersonnelInfo>>(Response.SUCCESS,"招募面试列表查询失败");
		}
		return resp;
	}

	/**
	 * 面试者--取得人才基本信息--用于面试评论
	 */
	@ApiOperation(value = "招聘面试人才信息查询", notes = "招聘面试人才信息查询", response = Response.class)
	@RequestMapping(value = "/getPersonnelInfoForViewer", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<AgentPersonnelInfo> getPersonnelInfoByViewer(@RequestParam String personnelId) {
		Response<AgentPersonnelInfo> resp = new Response<AgentPersonnelInfo>(Response.SUCCESS,"查询成功");
		try {
			AgentPersonnelInfo agentInfo = AppPersonnelService.getPersonnelInfoForViewer(personnelId);
			resp.setData(agentInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp = new Response<AgentPersonnelInfo>(Response.ERROR,"查询失败");
		}
		return resp;
	}
	
	/**
	 * 提交人才新的状态
	 *
	 */
	@ApiOperation(value = "更新人才信息", notes = "更新人才信息", response = Response.class)
	@RequestMapping(value = "/savePersonnelInfo", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<String> savePersonnelInfo(@RequestBody EnterVo enterVo) {
		Response<String> resp = null;
		try {
			Boolean canUpdate = AppPersonnelService.checkCanUpdateByPersonnelId(enterVo.getPersonnelId());
			if(!canUpdate){
				return new Response<>(Response.ERROR,"未通过更新检查");
			}
			AppPersonnelService.savePersonnelInfo(enterVo);
			resp = new Response<String>(Response.SUCCESS,"保存成功");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp = new Response<String>(Response.ERROR,"保存失败");
		}
		return resp;
	}

	
	/**
	 * 取得人才基本信息--所有人才表信息
	 * 签约明细--信息查询
	 */
	@ApiOperation(value = "取得人才基本信息", notes = "取得人才基本信息", response = Response.class)
	@RequestMapping(value = "/exclusions/getPersonnelBaseInfo", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<PersonnelBaseVo> getPersonnelBaseInfo(@RequestBody Map<String,String> map) {
		String personnelId = map.get("personnelId");
		if(map.isEmpty() || StringUtils.isEmpty(personnelId)){
			return new Response<>(400,"查询参数不能为空");
		}
		Response<PersonnelBaseVo> resp = new Response<PersonnelBaseVo>(Response.SUCCESS,"查询成功");
		try {
			resp.setData(AppPersonnelService.getPersonnelBaseInfo(personnelId));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp = new Response<PersonnelBaseVo>(Response.ERROR,"查询失败");
		}
		return resp;
	}
	
	/**
	 * 更新人才基本信息
	 * 签约明细--信息更新
	 */
	@ApiOperation(value = "更新人才基本信息", notes = "更新人才基本信息", response = Response.class)
	@RequestMapping(value = "/exclusions/updatePersonnelBaseInfo", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<String> updatePersonnelBaseInfo(@RequestBody PersonnelBaseVo personnelBaseVo) {
		Response<String> resp = null;
		try {
			AppPersonnelService.updatePersonnelBaseInfo(personnelBaseVo);
			resp = new Response<String>(Response.SUCCESS,"更新成功");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp = new Response<String>(Response.ERROR,"更新失败");
		}
		return resp;
	}
	
	/**
	 * 更新人才基本信息
	 * 签约明细--信息更新
	 */
	@ApiOperation(value = "更新人才基本信息", notes = "更新人才基本信息", response = Response.class)
	@RequestMapping(value = "/exclusions/updatePersonnel", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<String> updatePersonnel(@RequestBody Personnel personnel) {
		Response<String> resp = null;
		try {
			AppPersonnelService.updatePersonnel(personnel);
			resp = new Response<String>(Response.SUCCESS,"更新成功");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp = new Response<String>(Response.ERROR,"更新失败");
		}
		return resp;
	}
	
	/**
	 * 更新告知信息
	 */
	@ApiOperation(value = "更新告知信息", notes = "更新告知信息", response = Response.class)
	@RequestMapping(value = "/exclusions/updateInform", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<String> updateInform(@RequestBody PersonnelInformVo personnelInformVo) {
		Response<String> resp = null;
		try {
			AppPersonnelService.updatePersonnelInform(personnelInformVo);
			resp = new Response<String>(Response.SUCCESS,"更新成功");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp = new Response<String>(Response.ERROR,"更新失败");
		}
		return resp;
	}
	
	/**
	 * 获取告知信息
	 */
	@ApiOperation(value = "获取告知信息", notes = "获取告知信息", response = Response.class)
	@RequestMapping(value = "/exclusions/getInform", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<Map<String,Object>> getInform(@RequestBody Personnel personnel) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Response<Map<String,Object>> resp = null;
		try {
			resp = new Response<Map<String,Object>>(Response.SUCCESS,"获取成功");
			PersonnelInformVo personnelInformVo = AppPersonnelService.getInform(personnel);
			resultMap.put("personnelInformVo", personnelInformVo);
			if(personnelInformVo!=null && personnelInformVo.getPersonnelInformInfoList().size()>0){
				for(PersonnelInformInfo personnelInform:personnelInformVo.getPersonnelInformInfoList()){
					resultMap.put("f"+String.valueOf(personnelInform.getInformId()), personnelInform);
				}
			}
			resp.setData(resultMap);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp = new Response<Map<String,Object>>(Response.ERROR,"获取失败");
		}
		return resp;
	}
	
	/**
	 * 根据agentCode获取代理人信息
	 * @param personnel
	 * @return personnel
	 */
	@ApiOperation(value = "根据agentCode获取代理人信息", notes = "根据agentCode获取代理人信息", response = Response.class)
	@RequestMapping(value = "/getPersonnelByPresonnel", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<Personnel> getPersonnelByPresonnel(@RequestBody Personnel personnel) {
		Response<Personnel> resp = new Response<Personnel>(Response.SUCCESS,"数据获取成功");
		try {
			LoginUser account = tokenService.getLoginUser();
			Personnel resultPersonnel =new Personnel();
			if (account != null ) {
				personnel.setCellphone(account.getUser().getC_tel_mob());
				resultPersonnel = AppPersonnelService.getPersonnelByPresonnel(personnel);
			}
			resp.setData(resultPersonnel);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp = new Response<Personnel>(Response.ERROR,"数据获取失败");
		}
		return resp;
	}



	/**
	 * 更新人才基本信息
	 * 签约明细--清空银行卡信息
	 */
	@ApiOperation(value = "更新人才基本信息", notes = "更新人才基本信息", response = Response.class)
	@RequestMapping(value = "/exclusions/clearBankInfo", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<Boolean> clearBankInfo(@RequestBody Personnel personnel) {
		Response<Boolean> resp = null;
		Boolean aBoolean = null;
		try {
			aBoolean = AppPersonnelService.clearBankInfo(personnel);
			resp = new Response<Boolean>(Response.SUCCESS,"更新成功");
			resp.setData(aBoolean);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp = new Response<Boolean>(Response.ERROR,"更新失败");
			resp.setData(aBoolean);
		}
		return resp;
	}


	@ApiOperation(value = "查看个人信息", notes = "人才详情-个人信息", response = Response.class)
	@RequestMapping(value = "/getPersonnel", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<Map<String, Object>> getPersonnel(@RequestBody Personnel personnel) {
		System.err.println(personnel.getId());
		try {
			return personnelService.getPersonnel(personnel.getId());
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new Response<Map<String, Object>>(Response.ERROR,"查看个人信息失败");
		}
	}


	@ApiOperation(value = "APP招募邀请2", notes = "在线增员-个人信息", response = Response.class)
	@RequestMapping(value = "/exclusions/savePersonnel", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<String> savePersonnel(@RequestBody Personnel personnel) {
		try {
			return personnelService.saveInfo(personnel);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new Response<String>(Response.ERROR,"完善个人信息失败");
		}
	}



	@ApiOperation(value = "获取全部流程节点信息", notes = "获取全部流程节点信息", response = Response.class)
	@RequestMapping(value = "/getAllInterviewAction", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<List<FlowItemInterviewActionVo>> getAllInterviewAction(@RequestBody InterviewAction interviewAction) {
		Response<List<FlowItemInterviewActionVo>> resp = new Response<List<FlowItemInterviewActionVo>>(Response.SUCCESS, "全部流程节点数据获取成功");
		List<FlowItemInterviewActionVo> list= interviewActionService.getAllInterviewAction(interviewAction);
		if (list != null && list.size() > 0) {
			System.out.println(list.get(0));
		}
		resp.setData(interviewActionService.getAllInterviewAction(interviewAction));
		return resp;
	}


	@ApiOperation(value = "APP招募邀请1", notes = "在线增员邀请", response = Response.class)
	@RequestMapping(value = "/addPersonnel", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<Personnel> addPersonnel(@RequestBody Personnel personnelExpVo) {
		try {
			return personnelService.add(personnelExpVo);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new Response<Personnel>(Response.ERROR,"新增个人信息失败");
		}
	}


	@ApiOperation(value = "APP招募邀请邀请链接保存", notes = "在线增员邀请邀请链接保存", response = Response.class)
	@RequestMapping(value = "/addShare", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<ShareInfo> addShare(@RequestBody ShareInfo shareInfoVo) {
		try {
			return shareInfoService.addShare(shareInfoVo);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new Response<ShareInfo>(Response.ERROR,"APP招募邀请邀请链接保存失败");
		}
	}


	@ApiOperation(value = "检查url的时效",notes = "检查url的时效",response = Response.class)
	@RequestMapping(value = "/exclusions/checkUrl", method = RequestMethod.POST , produces = {"application/json;charset=utf-8"})
	public Response<Integer> checkUrl(@RequestBody ShareInfo shareInfo){
		try {
			Response<Integer> response = new Response<>(Response.SUCCESS, Response.SUCCESS_MESSAGE);
			//检查是否失效（被删除返回：1）
			int personnelCheckUrl = personnelService.checkUrl(shareInfo.getPersonnelId());
			if(personnelCheckUrl==1){
				response.setData(new Integer(1));
				response.setCode(Response.ERROR);
				response.setMsg("该招募流程已结束！");
				return response;
			}
			//检查时候超过60天（返回：2）
			int shareInfoCheckUrl = shareInfoService.checkUrl(shareInfo.getId());
			if(shareInfoCheckUrl==1){
				response.setData(new Integer(2));
				response.setCode(Response.ERROR);
				response.setMsg("您好，很抱歉的告诉您，由于您长时间未操作，该招募流程已结束！");
				return response;
			}
			return response;
		}catch (Exception e){
			return new Response<>(Response.ERROR,"链接不可用！");
		}
	}

	@ApiOperation(value = "查看个人信息", notes = "人才详情-个人信息", response = Response.class)
	@RequestMapping(value = "/exclusions/getPersonnel", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<Map<String, Object>> getExclusionsPersonnel(@RequestBody Personnel personnel) {
		System.out.println(personnel.getId());
		try {
			return personnelService.getPersonnel(personnel.getId());
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new Response<Map<String, Object>>(Response.ERROR,"查看个人信息失败");
		}
	}


	@ApiOperation(value = "APP招募邀请——新增个人信息", notes = "在线增员-个人信息", response = Response.class)
	@RequestMapping(value = "/exclusions/savePersonnelExp", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<String> savePersonnelExp(@RequestBody PersonnelVo1 personnelVo1) {
		try {
			TempCdeSales tempCdeSales = sysUserMapper.selectUserByPhone(personnelVo1.getPersonnel().getCellphone());
			if(tempCdeSales!=null){
				return new Response<String>(Response.ERROR,"该手机号已经是渤海员工");
			}
			return personnelService.saveInfo1(personnelVo1);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new Response<String>(Response.ERROR,"完善个人信息失败");
		}
	}




}
