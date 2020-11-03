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
import com.bpic.system.domain.Attachment;
import com.bpic.system.service.AttachmentService;
import com.fulan.api.personnel.domain.Check;
import com.fulan.api.personnel.domain.Personnel;
import com.fulan.api.personnel.domain.PersonnelInformInfo;
import com.fulan.api.personnel.domain.ShareInfo;
import com.fulan.api.personnel.vo.*;
import com.fulan.application.service.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
import java.util.Map;

@RestController
@RequestMapping("/web")
public class PersonnelListController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PersonnelListController.class);
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

    @Resource(name="AppPersonnelServiceImpl")
    AppPersonnelService AppPersonnelService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    TokenService tokenService;

    @Autowired
    SendSmsUtil sendSmsUtil;

    @PostMapping("/personnel/manage/personnel/personnelList")
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
     * 人才资料审核结果
     * @return
     */
    @PostMapping("/personnel/manage/personnel/checkSignResult")
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


    /**
     * @Author Mr.chen
     * @Description
     * //TODO
     * @Date
     * @Param
     * @return
     **/
    @ApiOperation(value = "签约转发", notes = "签约转发", response = Response.class)
    @PostMapping(value = "/personnel/manage/personnel/sendSignMessage")
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
            String url="http://mosales.bpic.com.cn/";
            res = personnelService.updateBatchPersonnelisSend(personnelId);
            if(res.getCode()==200){
                String position = personnelFlowItemVo.getProtocolPosition();
                if(personnelFlowItemVo.getConfirmPosition()!=null&&!"".equals(personnelFlowItemVo.getConfirmPosition())){
                    position = personnelFlowItemVo.getConfirmPosition();
                }
                //发送短信
                String url2 = url+"/app/web/home/#page=baseInfo&personnelId="+personnelId;
                String s = saveLink(url2);
                StringBuffer sb = new StringBuffer();
                sb.append("恭喜您通过培训,请在七日内填写资料进行签约.应聘职位[")
                        .append(position)
                        .append("]")
                        .append("签约链接[").append(s).append("]");
                System.out.println(sb.length()+"========"+sb.toString());
                ArrayList<String> list = sendSmsUtil.sendSms(phone, sb.toString());
                logger.info("调用短信接口返回状态=========================,{}",list);
                res.setMsg("转发成功");
            }
            return res;
        }catch (Exception e){
            logger.error("sendSignMessage:"+e);
            res = new Response<>(Response.ERROR,Response.ERROR_MESSAGE);
        }
        return res;
    }


    public String saveLink(String url){
        logger.info("saveLink入参"+url);
        LoginUser loginUser= tokenService.getLoginUser();

        ShareInfo shareInfo = new ShareInfo();
        shareInfo.setShareUrl(url);
        if(StringUtils.isNotNull(loginUser)){
            shareInfo.setCreateUser(loginUser.getUser().getC_emp_cde());
        }
        Response<ShareInfo> response = addShare(shareInfo);
        return response.getData().getShareUrl();
    }

    @ApiOperation(value = "链接保存", notes = "链接保存", response = Response.class)
    @RequestMapping(value = "/personnel/manage/personnel/addShare", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
    public Response<ShareInfo> addShare(@RequestBody ShareInfo shareInfoVo) {
        try {
            return shareInfoService.addShare(shareInfoVo);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new Response<ShareInfo>(Response.ERROR,"链接保存失败");
        }
    }

    @ApiOperation(value = "查看个人信息", notes = "人才详情-个人信息", response = Response.class)
    @RequestMapping(value = "/personnel/customer/app-personnel/getPersonnel", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
    public Response<Map<String, Object>> getPersonnel(@RequestBody Personnel personnel) {
        System.err.println(personnel.getId());
        try {
            return personnelService.getPersonnel(personnel.getId());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new Response<Map<String, Object>>(Response.ERROR,"查看个人信息失败");
        }
    }

    @ApiOperation(value = "获取个人审核图片信息", notes = "获取个人审核图片信息", response = Response.class)
    @RequestMapping(value = "/personnel/manage/personnel/getImgCheck", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
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



    /**
     * 培训审核处理
     * @param
     * @param
     * @param
     * @return
     */
    @PostMapping("/personnel/manage/personnel/checkTrainPersonnel")
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

    /**
     * 获取告知信息
     */
    @ApiOperation(value = "获取告知信息", notes = "获取告知信息", response = Response.class)
    @RequestMapping(value = "/personnel/customer/app-personnel/exclusions/getInform", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
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
     * 取得人才基本信息--所有人才表信息
     * 签约明细--信息查询
     */
    @ApiOperation(value = "取得人才基本信息", notes = "取得人才基本信息", response = Response.class)
    @RequestMapping(value = "/personnel/customer/app-personnel/exclusions/getPersonnelBaseInfo", produces = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
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
     * 根据category附件类型和hostId关联编号显示附件
     *
     * @param
     * @return
     */
    @ApiOperation(value = "根据category和hostId查找附件", notes = "根据category和hostId查找附件", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category", value = "附件类型", dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "hostId", value = "关联编号", dataType = "long", paramType = "query", example = "1") })
    @GetMapping(value = "/attachment/file/find")
    public Response<List<String>> findFileByTableAndHostId(@RequestParam("category") String category,
                                                           @RequestParam("hostId") Long hostId) {
        try {
            List<Attachment> attachmentlist = attachmentService.findFileByTableAndHostId(category, hostId);
            List<String> list = new ArrayList<String>();
            if (attachmentlist.size() == 0) {

                Attachment attachment=new Attachment();
                attachment.setPath("");
                Response<List<String>> resp = new Response<List<String>>(Response.SUCCESS, "无此图片");
                resp.setCode(Response.SUCCESS);
                list.add("http://mosales.bpic.com.cn/images/a3be0b095429b562ed47eec58b65aad.jpg");
                resp.setData(list);
                return resp;
            } else {
                // 存在图片情况
                if (attachmentlist.size() == 1) {
                    String str = attachmentlist.get(0).getPath();
                    list.add(str);
                } else {
                    for (Attachment attachment : attachmentlist) {
                        String str = attachment.getPath();
                        list.add(str);
                    }
                }
                Response<List<String>> resp = new Response<List<String>>(Response.SUCCESS, "回显成功");
                resp.setCode(Response.SUCCESS);
                resp.setData(list);
                return resp;
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new Response<List<String>>(Response.ERROR, "回显失败");
        }
    }
}
