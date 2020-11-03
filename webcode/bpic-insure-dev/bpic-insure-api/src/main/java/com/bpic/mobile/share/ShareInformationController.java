package com.bpic.mobile.share;

import com.bpic.common.constant.HttpStatus;
import com.bpic.common.core.controller.BaseController;
import com.bpic.common.core.domain.AjaxResult;
import com.bpic.common.core.domain.entity.ProductImage;
import com.bpic.common.core.domain.entity.SalesmanPerformance;
import com.bpic.common.core.domain.entity.ShareInformation;
import com.bpic.common.core.page.TableDataInfo;
import com.bpic.common.utils.StringUtils;
import com.bpic.common.utils.uuid.UUID;
import com.bpic.mobile.domain.vo.SalesmanPerformanceVo;
import com.bpic.mobile.domain.vo.VolumeVo;
import com.bpic.mobile.mapper.ShareInformationMapper;
import com.bpic.mobile.service.CustomizedService;
import com.bpic.mobile.service.SalesmanPerformanceService;
import com.bpic.mobile.service.ShareInformationService;
import com.bpic.system.domain.vo.SysLoginUserVo;
import com.bpic.system.service.ISysDictDataService;
import com.bpic.system.service.ISysLogininforService;
import com.bpic.system.service.ISysProductImageService;
import com.bpic.system.service.SysLoginUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @date 2020/8/18
 * @author changjj
 * @function 用户分享
 */
@RestController
@RequestMapping("/api/share")
public class ShareInformationController  extends BaseController {

    private static final Logger log= LoggerFactory.getLogger(ShareInformationController.class);

    @Autowired
    private ShareInformationService shareInformationService;

    @Autowired
    private ISysDictDataService iSysDictDataService;

    @Autowired
    private SysLoginUserService sysLoginUserService;

    @Autowired
    private SalesmanPerformanceService salesmanPerformanceService;

    @Autowired
    private ShareInformationMapper shareInformationMapper;

    @Autowired
    private ISysProductImageService sysProductImageService;

    /**
     * 自定义
     */
    @Autowired
    private CustomizedService customizedService;



    /**
     * 查询分享数据信息
     * @param map
     * @return AjaxResult
     */
    @PostMapping("/selectShare")
    public AjaxResult selectShare(@RequestBody Map<String,String> map){
        log.info("请求参数:{}",map);
        String telPhone = map.get("telPhone");
        if(StringUtils.isEmpty(telPhone)){
            return new AjaxResult(HttpStatus.BAD_REQUEST,"参数不能为空");
        }
        //0,1 过滤查询信息,方便生成productId分享id
        List<ShareInformation> shareInformations = shareInformationService.selectByTel(telPhone,1);
        for (ShareInformation shareInformation : shareInformations) {
            String money = shareInformationMapper.selectVolume(shareInformation.getShareProductId());
            shareInformation.setMoney(money);
        }
        log.info("返回结果集:{}",shareInformations);
        if(shareInformations.isEmpty()){
            return AjaxResult.fail(shareInformations);
        }
        return AjaxResult.success(shareInformations);
    }

    /**
     * 分享统计接口
     */
    @PostMapping("/selectClickTotal")
    public AjaxResult selectClickTotal(@RequestBody Map<String,String> map){
        log.info("请求参数==================================:{}",map);
        String telPhone = map.get("telPhone");
        if(StringUtils.isEmpty(telPhone)){
            return new AjaxResult(HttpStatus.BAD_REQUEST,"参数不能为空");
        }
        Integer total = shareInformationService.selectTotal(telPhone);
        return AjaxResult.success(total);
    }
    /**
     * 仿写分享统计接口
     */
    @PostMapping("/selectTotalAllReplicate")
    public AjaxResult selectTotalAllReplicate(@RequestBody Map<String,String> map){
        log.info("请求参数==================================:{}",map);
        String telPhone = map.get("telPhone");
        if(StringUtils.isEmpty(telPhone)){
            return new AjaxResult(HttpStatus.BAD_REQUEST,"参数不能为空");
        }
        Integer total = shareInformationService.selectTotalAllReplicate(telPhone);
        return AjaxResult.success(total);
    }
    /**
     * 88888888888888888888888888888888888自定义
     */
    @PostMapping("/selectClickCount")
    public AjaxResult selectClickCount(@RequestBody Map<String,String> map){
        log.info("请求参数==================================:{}",map);
        String telPhone = map.get("telPhone");
        if(StringUtils.isEmpty(telPhone)){
            return new AjaxResult(HttpStatus.BAD_REQUEST,"参数不能为空");
        }
        Integer total = customizedService.selectClickCount(telPhone);
        return AjaxResult.success(total);
    }

    /**
     * 生成分享Id
     */
    @PostMapping("/createProductId")
    public AjaxResult createProductId(@RequestBody Map<String,String> map){
        log.info("生成分享Id接口start=============================,{}",map);
        String telPhone = map.get("telPhone");
        String type = map.get("type");
        String productCode = map.get("productCode");
        if(StringUtils.isEmpty(telPhone)||StringUtils.isEmpty(type)||StringUtils.isEmpty(productCode)){
            return new AjaxResult(HttpStatus.BAD_REQUEST,"参数不能为空");
        }
        String productId =UUID.randomUUID().toString().replaceAll("-", "");
        log.info("生成分享Id=================,{}",productId);
        ShareInformation shareInformation = shareInformationService.selectByProductId(productId);
        if(shareInformation!=null){
            return new AjaxResult(HttpStatus.CONFLICT,"已存在该id，请重新请求");
        }
        SysLoginUserVo sysLoginUserVo = sysLoginUserService.selectByCellphone(telPhone);
        ShareInformation shareInfor=new ShareInformation();
        shareInfor.setTelPhone(telPhone);
        shareInfor.setShareProductId(productId);
        shareInfor.setSharerEmpno(sysLoginUserVo.getC_emp_cde2());
        shareInfor.setSharerSuperiorEmpno(sysLoginUserVo.getC_emp_cde());
        shareInfor.setSharerName(sysLoginUserVo.getUser_name());
        shareInfor.setSharerSuperiorPhone(sysLoginUserVo.getCphone());
        shareInfor.setSharerSuperiorName(sysLoginUserVo.getC_emp_cnm());
        shareInfor.setType(type);
        shareInfor.setProductCode(productCode);
        try {
            shareInformationService.insertByProductId(shareInfor);
            log.info("生成分享Id接口end=============================,{}",productId);
        }catch (Exception e){
            log.error("分享接口异常,{}",e.getMessage());
        }
        return AjaxResult.success(productId);
    }

    /**
     * 保存分享信息
     * @param shareInformation
     * @return AjaxResult
     */
    @PostMapping("/insertShareInformation")
    public AjaxResult insertShareInformation(@RequestBody ShareInformation shareInformation){
        log.info("保存分享信息接口start===========================,{}",shareInformation);
        if(shareInformation==null||StringUtils.isEmpty(shareInformation.getShareProductId())){
            return new AjaxResult(HttpStatus.BAD_REQUEST,"请求参数不能为空");
        }
        if(shareInformationService.selectByProductId(shareInformation.getShareProductId())==null){
            return new AjaxResult(HttpStatus.SUCCESS,"不存在该分享对象");
        }
        try {
            shareInformationService.insertShareInformation(shareInformation);
        }catch (Exception e){
            log.error("保存分享信息失败,{}============================",e.getMessage());
            return AjaxResult.error("保存分享信息失败,请联系管理员");
        }
        log.info("保存分享信息接口end===========================");
        return AjaxResult.success();
    }
    /**
     * 自定义插入数据
     **/
    @PostMapping("/insertData")
    public AjaxResult insertData(@RequestBody ShareInformation shareInformation){
        log.info("保存分享信息接口start===========================,{}",shareInformation);
        if(shareInformation==null||StringUtils.isEmpty(shareInformation.getShareProductId())){
            return new AjaxResult(HttpStatus.BAD_REQUEST,"请求参数不能为空");
        }
        try {
            customizedService.insertData(shareInformation);
        }catch (Exception e){
            log.error("保存分享信息失败,{}============================",e.getMessage());
            return AjaxResult.error("保存分享信息失败,请联系管理员");
        }
        log.info("保存分享信息接口end===========================");
        return AjaxResult.success();
    }

    /**
     * 自定义更新
     */
    @PostMapping("/updateTelphone")
    public AjaxResult updateTelphone(@RequestBody ShareInformation s){
        customizedService.updateTelPhone(s);
        return AjaxResult.success();
    }


    /**
     * 自定义删除
     */
    @PostMapping("/deleteData")
    public AjaxResult deleteData(@RequestBody ShareInformation s){
        log.info("以tel_phone来删除信息start===========================");
        customizedService.deleteData(s);

        return AjaxResult.success();

    }

    /**
     * 更新点击量
     * @param shareInformation
     * @return AjaxResult
     */
    @PostMapping("/refreshClick")
    public AjaxResult refreshClick(@RequestBody ShareInformation shareInformation){
        log.info("请求参数=====================================,{}",shareInformation);
        if(shareInformation==null||StringUtils.isEmpty(shareInformation.getShareProductId())){
            return new AjaxResult(HttpStatus.BAD_REQUEST,"参数不能为空");
        }
        if(shareInformationService.selectByProductId(shareInformation.getShareProductId())==null){
            return new AjaxResult(HttpStatus.SUCCESS,"分享不存在");
        }
        shareInformation.setClick(shareInformationService.selectClick(shareInformation)+1);
        try {
            shareInformationService.refreshClick(shareInformation);
        }catch (Exception e){
            log.info("捕捉异常============================================,{}",e.getMessage());
            return AjaxResult.error("系统异常,请联系管理员");
        }
        return AjaxResult.success();
    }

    /**
     * 查分享开关
     */
    @GetMapping("/getDictData")
    public AjaxResult getDictData(){
        log.info("查询分享开关接口start================================");
        String dictLabel = iSysDictDataService.selectDictLabel("is_allow_share", "shareSwitch");
        if(StringUtils.isEmpty(dictLabel)){
            dictLabel="false";
        }
        log.info("查询分享开关接口end==================================,{}",dictLabel);
        return AjaxResult.success(dictLabel);
    }

    /**
     * 查分享成交信息详情
     */
    @GetMapping("/queryAmount")
    public TableDataInfo queryAmount(@RequestParam("shareProductId") String id){
        log.info("查询分享成交信息详情start======================,id:{}",id);
        startPage();
        List<SalesmanPerformanceVo> performanceVos =  salesmanPerformanceService.queryVolumeById(id);
        log.info("查询分享成交信息详情end======================,List<SalesmanPerformanceVo>:{}",performanceVos);
        return getDataTable(performanceVos);
    }

    /**
     * 查询对应产品图片
     */
    @GetMapping("/getProductImage")
    public AjaxResult getProductImage(@RequestParam("productCode") String productCode ){
        log.info("查询对应产品图片start======================,productCode:{}",productCode);
        if(StringUtils.isEmpty(productCode)){
            return new AjaxResult(HttpStatus.BAD_REQUEST,"参数不能为空");
        }
        ProductImage productImage = sysProductImageService.selectByProductCode(productCode);
        log.info("查询对应产品图片end======================,productCode:{}",productImage.getImage());
        return AjaxResult.success(productImage.getImage());
    }

    /**
     * 是否允许分享出单开关
     */
    @GetMapping("/updateIsShare")
    public AjaxResult updateIsShare(@RequestParam("shareProductId") String id,@RequestParam("isShare") String isShare){
        log.info("是否允许分享出单开关start======================,id:{},isShare:{}",id,isShare);
        if(StringUtils.isEmpty(id)||StringUtils.isEmpty(isShare)){
            return new AjaxResult(HttpStatus.BAD_REQUEST,"请求参数不能为空");
        }
        try {
            shareInformationService.updateIsShare(id,isShare);
        }catch (Exception e){
            return AjaxResult.error("系统错误，请联系管理员");
        }
        log.info("是否允许分享出单开关end======================");
        return AjaxResult.success();
    }

    /**
     * 查询分享开关
     */
    @GetMapping("selectIsShare")
    public AjaxResult selectIsShare(@RequestParam("shareProductId") String id){
        log.info("查询分享开关start======================,id:{}",id);
        if(StringUtils.isEmpty(id)){
            return new AjaxResult(HttpStatus.BAD_REQUEST,"请求参数不能为空");
        }
        ShareInformation shareInformation = shareInformationService.selectByProductId(id);
        if(shareInformation==null){
            return AjaxResult.error("不存在该分享链接");
        }
        log.info("查询分享开关end======================");
        return AjaxResult.success(shareInformation.getIsShare());
    }
}
