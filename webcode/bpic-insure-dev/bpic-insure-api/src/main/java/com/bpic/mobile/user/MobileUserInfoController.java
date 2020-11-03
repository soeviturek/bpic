package com.bpic.mobile.user;

import com.bpic.common.config.BpicConfig;
import com.bpic.common.core.controller.BaseController;
import com.bpic.common.core.domain.AjaxResult;
import com.bpic.common.core.domain.entity.SysLoginUser;
import com.bpic.common.core.domain.entity.TdepartmentSales;
import com.bpic.common.core.domain.model.LoginUser;
import com.bpic.common.core.page.TableDataInfo;
import com.bpic.common.utils.StringUtils;
import com.bpic.common.utils.file.FileUploadUtils;
import com.bpic.framework.web.service.TokenService;
import com.bpic.mobile.weixin.BASE64DecodedMultipartFile;
import com.bpic.system.domain.AuditImage;
import com.bpic.system.domain.vo.AuditImageVo;
import com.bpic.system.domain.vo.SysLoginUserVo;
import com.bpic.system.domain.vo.TempCdeSalesVo;
import com.bpic.system.service.AuditImageService;
import com.bpic.system.service.ISysDeptService;
import com.bpic.system.service.ISysUserService;
import com.bpic.system.service.impl.SysLoginUserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/userinfo")
public class MobileUserInfoController extends BaseController {


    private static final Logger logger = LoggerFactory.getLogger(MobileUserInfoController.class);

    @Resource
    SysLoginUserServiceImpl ISysLoginUserService;

    @Autowired
    public RedisTemplate<String, String> redisTemplate;

    @Resource
    AuditImageService auditImageService;

    @Value("${bpic.imageUrl}")
    private String imageUrl;

    @Resource
    ISysUserService iSysUserService;

    @Resource
    ISysDeptService iSysDeptService;

    @Autowired
    private TokenService tokenService;

    /**
     * 用户信息查询
     * @return
     */
    @PostMapping("/queryUserInfo")
    public AjaxResult queryUserInfo(HttpServletRequest request,@RequestBody HashMap<String, String> requestBody){
        logger.info("==============开始请求queryUserInfo接口==============");
        String cellphone = requestBody.get("cellphone");
        if(StringUtils.isEmpty(cellphone)){
            return new AjaxResult(400,"手机号不能为空");
        }
        try{
            //获取认证状态
            String auditStatus;
            AuditImageVo auditImageVo = auditImageService.selectAuditImage(cellphone);
            if(auditImageVo == null){
                auditStatus = "0";
            }else {
                auditStatus = auditImageVo.getAudit_status();
            }
            SysLoginUserVo sysLoginUserVo = ISysLoginUserService.selectByCellphone(cellphone);

            if(sysLoginUserVo != null){
                String parentDeptName;
                if(StringUtils.isEmpty(sysLoginUserVo.getC_dpt_cde())){
                    logger.info("=======内部员工所属dpt_cde为空=====");
                    TempCdeSalesVo tempCdeSalesVo = iSysUserService.selectUserById(Long.valueOf(sysLoginUserVo.getC_emp_cde2()));
                    logger.info("=======根据员工编号查询渤海业务员表,获取所属公司编码：{}=====",tempCdeSalesVo.getC_dpt_cde());
                    List<TdepartmentSales> tdepartmentSales = iSysDeptService.selectOwnDeptList();
                    logger.info("渤海内部员工:{}",tempCdeSalesVo.getC_dpt_cde());
                    parentDeptName = iSysUserService.getParentDeptName(tdepartmentSales, new StringBuffer(), tempCdeSalesVo.getC_dpt_cde());
                    logger.info("渤海内部员工所属公司查询{}",parentDeptName);
                }else{
                    logger.info("=======下线员工所属dpt_cde：{}=====",sysLoginUserVo.getC_dpt_cde());
                    List<TdepartmentSales> tdepartmentSales = iSysDeptService.selectOwnDeptList();
                    logger.info("下线内部员工:{}",sysLoginUserVo.getC_dpt_cde());
                    parentDeptName = iSysUserService.getParentDeptName(tdepartmentSales, new StringBuffer(), sysLoginUserVo.getC_dpt_cde());
                    logger.info("下线员工所属公司查询{}",parentDeptName);
                }
                Map<String, Object> map = new HashMap<>();
                map.put("info",sysLoginUserVo);
                map.put("auditStatus",auditStatus);
                map.put("dept",parentDeptName);
                logger.info("===========请求queryUserInfo结束================");
                return new AjaxResult(200,"查询个人信息成功",map);
            }
            return new AjaxResult(400,"个人信息不存在");
        }catch (Exception e){
            logger.info("个人信息查询失败,{}",e.getMessage());
            return new AjaxResult(500,"查询失败");
        }
    }

    /**
     * 用户信息更新
     * @return
     */
    @PostMapping("/updateCellphone")
    public AjaxResult updateUserInfo(HttpServletRequest request,@RequestBody HashMap<String, String> requestBody){
        logger.info("===========请求updateCellphone开始================");
        String old = requestBody.get("old");
        String aNew = requestBody.get("new");
        String code = requestBody.get("code");
        String open_id = requestBody.get("open_id");
        if(StringUtils.isEmpty(old)){
            return new AjaxResult(400,"旧手机号不能为空");
        }
        if(StringUtils.isEmpty(aNew)){
            return new AjaxResult(400,"新手机号不能为空");
        }
        if(StringUtils.isEmpty(aNew)){
            return new AjaxResult(400,"短信验证码不能为空");
        }
        if(StringUtils.isEmpty(open_id)){
            return new AjaxResult(400,"open_id不能为空");
        }
        try{
            String redisCode = redisTemplate.opsForValue().get(old);
            if(StringUtils.isEmpty(redisCode)){
                return new AjaxResult(400,"验证码已失效,请重新发送");
            }
            if(code.equals(redisCode)){
                return ISysLoginUserService.updateUserInfo(old,aNew, open_id);
            }
            return new AjaxResult(400,"验证码不一致");

        }catch (Exception e){
            logger.info("个人信息更新失败,{}",e.getMessage());
            return new AjaxResult(500,"更新失败");
        }
    }

    /**
     * 用户信息新增
     * @return
     */
    @PostMapping("/insertUserInfo")
    public AjaxResult insertUserInfo(HttpServletRequest request,@RequestBody SysLoginUser sysLoginUser){
        logger.info("===========请求insertUserInfo开始================");
        try{
            //cellphone 新增
            int i = ISysLoginUserService.insertUser(sysLoginUser);
            logger.info("===========请求insertUserInfo结束================");
            if(i > 0){
                return new AjaxResult(200,"新增个人信息成功");
            }
            return new AjaxResult(400,"新增个人信息失败");
        }catch (Exception e){
            logger.info("个人信息插入失败,{}",e.getMessage());
            return new AjaxResult(500,"新增失败");
        }
    }

    /**
     * 根据手机号更新其他信息
     * @param sysLoginUser
     * @return
     */
    @PostMapping("/updateUserInfo")
    public AjaxResult updateUserInfo(@RequestBody SysLoginUser sysLoginUser){
        logger.info("===========请求updateUserInfo开始================");
        try{
            //cellphone 新增
            int i = ISysLoginUserService.updateUserByCellphone(sysLoginUser);
            logger.info("===========请求updateUserInfo结束================");
            if(i > 0){
                return new AjaxResult(200,"更新个人信息成功");
            }
            return new AjaxResult(400,"更新个人信息失败");
        }catch (Exception e){
            logger.info("更新个人信息失败,{}",e.getMessage());
            return new AjaxResult(400,"更新个人信息失败");
        }
    }

    /**
     * 业务员和下线关系查询接口
     * @return AjaxResult
     */
    @PostMapping("/empRelation")
    public AjaxResult empRelation(@RequestBody HashMap<String, String> requestBody){
        logger.info("===========请求empRelation开始================");
        String cellphone = requestBody.get("cellphone");
        String user_name = requestBody.get("user_name");
        String start_time = requestBody.get("start_time");
        String end_time = requestBody.get("end_time");
        if(StringUtils.isEmpty(cellphone)){
            return new AjaxResult(400,"手机号不能为空");
        }
        try{
            startPage();
            List<SysLoginUser> users = ISysLoginUserService.selectSxUser(cellphone,user_name,start_time,end_time);
            TableDataInfo dataTable = getDataTable(users);
            logger.info("===========请求empRelation结束================");
            return new AjaxResult(200,"操作成功",dataTable);
        }catch (Exception e){
            logger.info("业务员和下线关系查询接口异常,{}",e.getMessage());
            return new AjaxResult(500,"查询失败");
        }
    }
    /**
     * 图片上传
     */
    @RequestMapping("/uploadImage")
    public AjaxResult upload(HttpServletRequest request,
                             @RequestParam("file1")String file1,
                              @RequestParam("file2")String file2,
                             @RequestParam("cellphone")String cellphone){
        String contentType = request.getHeader("Content-Type");
        logger.info("===========请求uploadImage开始================");
        logger.info("文件上传接口的contentType:{}",contentType);
        if(StringUtils.isEmpty(cellphone)){
            return new AjaxResult(400,"手机号不能为空");
        }        // 获取上传文件路径
        logger.info("文件上传cellphone:{}",cellphone);

        String filePath = BpicConfig.getUploadPath();
        logger.info("文件存储系统路径为:{}",filePath);
        HashMap<String, String> map = new HashMap<>();
        try {
            //正面， 上传后返回的文件名
            String fileNames = FileUploadUtils.upload(filePath,BASE64DecodedMultipartFile.base64ToMultipart(file1));
            logger.info("上传完成后返回的文件名:{}",fileNames);
            String url = imageUrl + fileNames;
            logger.info("上传完成保存到服务器的全路径是:{}",url);
            //将返回的全路径放进map
            map.put("front",url);
        }
        catch (Exception e) {
            logger.info("图片上传失败,{}",e.getMessage());
            return new AjaxResult(500,"图片上传失败");
        }
        try {
            //反面  上传后返回的文件名
            String fileNames = FileUploadUtils.upload(filePath,BASE64DecodedMultipartFile.base64ToMultipart(file2));
            String url = imageUrl + fileNames;
            //将返回的全路径放进map
            logger.info("上传完成保存到服务器的全路径是:{}",url);
            map.put("back",url);
        }
        catch (Exception e) {
            logger.info("图片上传失败,{}",e.getMessage());
            return new AjaxResult(500,"图片上传失败");
        }
        //上传完成后将两个文件取出来入库和手机号绑定
        AuditImage image = new AuditImage();
        image.setFront_path(map.get("front"));
        image.setBack_path(map.get("back"));
        image.setC_tel_mob(cellphone);
        //更新审核状态
        image.setAudit_status("1");
        //返回审核状态
        map.put("auditStatus","1");
        logger.info("===========请求uploadImage结束================");
        try{//先查有没有
            AuditImageVo auditImageVo = auditImageService.selectAuditImage(cellphone);
            if(auditImageVo != null){
                //有更新
                auditImageService.updateAuditImage(image);
                return new AjaxResult(200,"图片保存成功",map);
            }
            //没有插入
            int i = auditImageService.insertAuditImage(image);
            if(i > 0){
                logger.info("===========请求uploadImage结束================");
                return new AjaxResult(200,"图片保存成功",map);
            }
            return new AjaxResult(200,"图片保存失败");
        }catch (Exception e){
            logger.info("图片保存失败,{}",e.getMessage());
            return new AjaxResult(500,"图片保存失败");
        }
    }

    /**
     * 返回下线总数
     */
    @GetMapping("/empRelCount")
    public AjaxResult empRelCount(@RequestParam String cellphone){
        logger.info("===========请求empRelCount开始================");
        if(StringUtils.isEmpty(cellphone)){
            return new AjaxResult(400,"手机号不能为空");
        }
        try{
            //通过二维码分享的下线总数
            Integer count= ISysLoginUserService.selectCount(cellphone);
            logger.info("===========请求empRelation结束================");
            return new AjaxResult(200,"操作成功",count);
        }catch (Exception e){
            logger.info("业务员和下线关系查询接口异常,{}",e.getMessage());
            return new AjaxResult(500,"查询失败");
        }
    }

    /**
     * 返回激活下线总数
     */
    @GetMapping("/activeCount")
    public AjaxResult activeCount(@RequestParam String cellphone){
        logger.info("===========请求activeCount开始================");
        if(StringUtils.isEmpty(cellphone)){
            return new AjaxResult(400,"手机号不能为空");
        }
        try{
            List<SysLoginUser> sysLoginUsers = ISysLoginUserService.selectSxUser(cellphone,null,null,null);
            if(!sysLoginUsers.isEmpty()){
                List<SysLoginUser> collect = sysLoginUsers.stream()
                        .filter(sysLoginUser -> StringUtils.isNotEmpty(sysLoginUser.getC_emp_cde2()))
                        .collect(Collectors.toList());
                return new AjaxResult(200,"操作成功", collect.size());
            }
            return new AjaxResult(200,"操作成功",0);
        }catch (Exception e){
            logger.info("业务员和下线关系查询接口异常,{}",e.getMessage());
            return new AjaxResult(500,"查询失败");
        }
    }

    /**
     * 激活渠道详情
     * @param requestBody
     * @return
     */
    @PostMapping("/activeEmp")
    public AjaxResult activeEmp(@RequestBody HashMap<String, String> requestBody){
        logger.info("===========请求activeEmp开始================");
        String user_name = requestBody.get("user_name");
        String start_time = requestBody.get("start_time");
        String end_time = requestBody.get("end_time");
        String cellphone = requestBody.get("cellphone");
        if(StringUtils.isEmpty(cellphone)){
            return new AjaxResult(400,"手机号不能为空");
        }
        try{
            List<SysLoginUser> sysLoginUsers = ISysLoginUserService.selectSxUser(cellphone,user_name,start_time,end_time);
            if(!sysLoginUsers.isEmpty()){
                startPage();
                List<SysLoginUser> collect = sysLoginUsers.stream()
                        .filter(sysLoginUser -> StringUtils.isNotEmpty(sysLoginUser.getC_emp_cde2()))
                        .collect(Collectors.toList());
                TableDataInfo dataTable = getDataTable(collect);
                logger.info("===========请求activeEmp结束================");
                return new AjaxResult(200,"操作成功",dataTable);
            }
            return new AjaxResult(200,"操作成功",null);
        }catch (Exception e){
            logger.info("业务员和下线关系查询接口异常,{}",e.getMessage());
            return new AjaxResult(500,"查询失败");
        }
    }

    /**
     * 禁用/解禁
     * @param del_flag
     * @param cellphone
     * @return
     */
    @GetMapping("/forbidden")
    public AjaxResult forbidden(@RequestParam("del_flag") String del_flag,
                                @RequestParam("cellphone")String cellphone){
        if(StringUtils.isEmpty(del_flag)){
            return new AjaxResult(400,"禁用状态不能为空");
        }
        if(StringUtils.isEmpty(cellphone)){
            return new AjaxResult(400,"手机号不能为空");
        }
        SysLoginUser sysLoginUser = new SysLoginUser();
        sysLoginUser.setC_tel_mob(cellphone);
        sysLoginUser.setDel_flag(del_flag);
        int i = ISysLoginUserService.updateUserByCellphone(sysLoginUser);
        if(i > 0){
            return new AjaxResult(200,"禁用或解禁成功");
        }
        return new AjaxResult(500,"禁用或解禁失败");
    }

    /**
     * 退出登录
     * @return
     */
    @GetMapping("/userLogout")
    public AjaxResult logout(HttpServletRequest request){
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            String userName = loginUser.getUsername();
            logger.info("-----------------------删除用户缓存记录--------------------------");
            tokenService.delLoginUser(loginUser.getToken());
            logger.info("-----------------------记录用户退出日志--------------------------");
            return new AjaxResult(200,"用户"+userName+"退出成功");
        }
        return new AjaxResult(200,"退出成功");
    }
}
