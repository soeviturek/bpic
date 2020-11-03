package com.bpic.web.controller.system;

import com.bpic.common.constant.HttpStatus;
import com.bpic.common.core.controller.BaseController;
import com.bpic.common.core.domain.AjaxResult;
import com.bpic.common.core.domain.entity.Product;
import com.bpic.common.core.domain.entity.ProductConfig;
import com.bpic.common.core.domain.entity.TempCdeSales;
import com.bpic.common.core.page.TableDataInfo;
import com.bpic.common.core.redis.RedisCache;
import com.bpic.common.utils.StringUtils;
import com.bpic.mobile.product.ChannelServerData;
import com.bpic.mobile.product.ChannelServerProduct;
import com.bpic.system.domain.vo.*;
import com.bpic.system.service.ISysDeptService;
import com.bpic.system.service.ISysProductConfigService;
import com.bpic.web.core.channel.ChannelProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/web/system/productConfig")
@Transactional(rollbackFor = Exception.class)
public class SysProductConfigController extends BaseController {

    public static final Logger log = LoggerFactory.getLogger(SysProductConfigController.class);
    @Autowired
    private ISysProductConfigService sysProductConfigService;

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private RedisCache redisCache;

    @Resource
    private ChannelProductServiceImpl channelProductService;

    public final String CACHE_PRODUCT_KEY = "productData";

    /**
     *获取产品配置信息列表
     * @param productConfig
     * @return
     */
    @PostMapping("/getProductConfigList")
    public TableDataInfo getProductConfigList(@RequestBody ProductConfigVo productConfig){
        if(productConfig==null){
            TableDataInfo tableDataInfo = new TableDataInfo();
            tableDataInfo.setCode(HttpStatus.BAD_REQUEST);
            tableDataInfo.setMsg("请求参数为空");
            return tableDataInfo;
        }
        log.info("productConfig:{}",productConfig);
        if(!productConfig.getDeptId().isEmpty()){
            List<String> list = sysDeptService.selectDeptCodeList(productConfig.getDeptId());
            productConfig.setList(list);
        }
        startPage();
        List<ProductConfig> productConfigList = sysProductConfigService.getProductConfigList(productConfig);
        return getDataTable(productConfigList);
    }


    /**
     * 添加员工
     */
    @RequestMapping("/insertSales")
    public AjaxResult insertSales(@RequestBody Map<String,Object> map){
        log.info("新增员工接口start========================,{}",map);
        String configCode = (String) map.get("configCode");
        String deptId = (String) map.get("deptId");
        String flage = (String) map.get("flage");
        List<String> userCode = (List<String>) map.get("userCode");
        if(StringUtils.isEmpty(configCode)||StringUtils.isEmpty(deptId)||StringUtils.isEmpty(flage)){
            return new AjaxResult(HttpStatus.BAD_REQUEST,"参数不允许为空");
        }
        try {
            sysProductConfigService.insertSales(configCode,deptId,flage,userCode);
        }catch (Exception e){
            log.error("新增员工接口保存失败=========================,{}",e.getMessage());
            return AjaxResult.error("保存失败，系统错误");
        }
        log.info("新增员工接口end====================");
        return AjaxResult.success("保存成功");
    }

    /**
     * 删除员工
     * @param map
     */
    @PostMapping("/deleteSales")
    public AjaxResult deleteSales(@RequestBody Map<String,String> map){
        String configCode = map.get("configCode");
        String sales = map.get("sales");
        if(StringUtils.isEmpty(sales)&&StringUtils.isEmpty(configCode)){
            return new AjaxResult(HttpStatus.BAD_REQUEST,"请求参数不能为空");
        }
        try {
            sysProductConfigService.deleteSales(sales,configCode);
        }catch (Exception e){
            log.error("删除人员失败,{}=======================",e.getMessage());
            return AjaxResult.error("删除人员失败");
        }

        return AjaxResult.success();
    }

    /**
     * 获取产品配置详情
     * @param configCode
     * @return
     */
    @GetMapping("/findProductConfigById")
    public AjaxResult findProductConfigById(String configCode){
        if(StringUtils.isEmpty(configCode)){
            return new AjaxResult(HttpStatus.BAD_REQUEST,"参数不允许为空");
        }
        ProductConfig productConfig = sysProductConfigService.findProductConfigById(configCode);
        return AjaxResult.success(productConfig);
    }

    /**
     * 获取产品详情
     * @param map
     * @return
     */
    @PostMapping("/findProductById")
    public AjaxResult findProductById(@RequestBody Map<String,String> map){
        String configCode=map.get("configCode");
        if(StringUtils.isEmpty(configCode)){
            return AjaxResult.error("配置权限代码不能为空");
        }
        String productCode = map.get("productCode");
        String productName=map.get("productName");
        startPage();
        List<Product> productList = sysProductConfigService.findProductById(productCode,productName,configCode);
        return AjaxResult.success(productList);
    }

    /**
     * 获取对应人员信息
     * @param map
     * @return
     */
    @PostMapping("/findSales")
    public TableDataInfo findSales(@RequestBody Map<String,String> map){
        String sales=map.get("sales");//员工编号
        String salesName=map.get("salesName");
        String configCode=map.get("configCode");
        log.info("sales:{},salesName:{},configCode{}",sales,salesName,configCode);
        if(StringUtils.isEmpty(configCode)){
            log.error("参数为空,[]",configCode);
            TableDataInfo tableDataInfo = new TableDataInfo();
            tableDataInfo.setCode(HttpStatus.BAD_REQUEST);
            tableDataInfo.setMsg("配置权限代码不能为空");
            return tableDataInfo;
        }
        startPage();
        List<SalesVo> salesList = sysProductConfigService.findSales(sales, salesName, configCode);
        List<TempCdeSalesVo> list=new ArrayList<>();
        for (SalesVo salesVo : salesList) {
            TempCdeSalesVo tempCdeSales=new TempCdeSalesVo();
            tempCdeSales.setC_emp_cnm(salesVo.getCEmpCnm());
            tempCdeSales.setC_salegrp_cde(salesVo.getCSalegrpCde());
            tempCdeSales.setC_emp_cde(salesVo.getSales());
            tempCdeSales.setParentDeptCode(salesVo.getDeptName());
            list.add(tempCdeSales);
        }
        return getDataTable(list,salesList);
    }

    /**
     * 新增或者修改配置信息
     * @param productSalesConfigVo
     * @return
     */
    @PostMapping("/insertOrUpdateProductConfig")
    public AjaxResult insertOrUpdateProductConfig(@RequestBody ProductSalesConfigVo productSalesConfigVo) throws ParseException {
        log.info("新增或者修改配置信息接口start==============================,{}",productSalesConfigVo);
        List<TempCdeSales> salesList = productSalesConfigVo.getSales();
        ProductConfig productConfig = productSalesConfigVo.getProductConfig();
        List<Product> productList = productSalesConfigVo.getProduct();
        log.info("salesList:{},productConfig:{},productList{}",salesList,productConfig,productList);
        String flage = productSalesConfigVo.getFlage(); //判断是暂存还是保存
        if(productConfig==null&&salesList.isEmpty()&&productList.isEmpty()){
            return new AjaxResult(HttpStatus.BAD_REQUEST,"参数不能为空");
        }
        if(!StringUtils.isEmpty(flage)){
            if(sysProductConfigService.findProductConfigById(productConfig.getConfigCode().toString())!=null){
                return new AjaxResult(HttpStatus.CONFLICT,"请求资源重复,请重新请求");
            }
        }
        ProductConfig config = sysProductConfigService.findProductConfigById(String.valueOf(productConfig.getConfigCode()));
        if(config!=null){
            if (productConfig.getStatus() == 2) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date parse = simpleDateFormat.parse(productConfig.getAuthEndTime());
                if (parse.before(new Date())) {
                    return new AjaxResult(HttpStatus.SUCCESS, "产品生效日期已过");
                } else {
                    productConfig.setStatus(1);
                }
            } else if (productConfig.getStatus() == 0 || productConfig.getStatus() == 3) {
                productConfig.setStatus(2);
            }
        }else {
            if(flage.equals("0")){
                productConfig.setStatus(3);
            }else if(flage.equals("1")){
                productConfig.setStatus(2);
            }
        }
        try {
            AjaxResult ajaxResult = sysProductConfigService.insertOrUpdate(config, salesList, productConfig, productList);
            log.info("保存接口end================================");
            return ajaxResult;
        }catch (Exception e){
            log.error("保存接口异常==================================,{}",e.getMessage());
            return AjaxResult.error("接口异常");
        }
    }

    /**
     * 下线
     * @param configCode
     * @return
     */
    @GetMapping("/offline")
    public AjaxResult updateProductConfig(String configCode){
        try {
            sysProductConfigService.updateStatus(configCode);
        }catch (Exception e){
            log.error("操作失败,[]"+e.getMessage());
            return AjaxResult.error("操作失败,请联系管理员");
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 级联查询机构
     * @param deptId
     * @return
     */
    @GetMapping("/selectDept/{deptId}")
    public AjaxResult selectDept(@PathVariable String deptId){
        List<DeptVo> tdepartmentSales = new ArrayList<>();
        if(deptId.equals("00")){
            DeptVo deptVo=new DeptVo();
            deptVo.setC_dpt_cde("0");
            deptVo.setC_dpt_abr("全部");
            tdepartmentSales.add(deptVo);
        }
        List<DeptVo> deptVos = sysDeptService.selectChildDeptList(deptId);
        for (DeptVo deptVo : deptVos) {
            tdepartmentSales.add(deptVo);
        }
        return AjaxResult.success(tdepartmentSales);
    }

    /**
     * 查询产品类型
     */
    @GetMapping("/selectProductType")
    public AjaxResult selectProductType(){
        AjaxResult ajaxResult = null;
        ChannelServerData channelServerData = channelProductService.productTypesFromChannel();
        if(channelServerData==null){
            ajaxResult = AjaxResult.fail("没有查询到相关数据!");
        }else {
            ajaxResult = AjaxResult.success(channelServerData);;
        }
        return ajaxResult;
    }


    /**
     * 查询产品种类
     * @param map
     * @return
     */
    @PostMapping("/selectProduct")
    public AjaxResult selectProduct(@RequestBody Map<String,String> map){
        AjaxResult ajaxResult = null;
        //List<ChannelServerProduct> cacheList = redisCache.getCacheObject(CACHE_PRODUCT_KEY);
        String productName = map.get("productName");
        String productCode = map.get("productCode");
        log.info("productName:{},productCode:{}",productName,productCode);
        //if(cacheList.isEmpty()){
        try {
            ChannelServerData data = channelProductService.productListFromChannel("01");
            if (data.getGroups().isEmpty()) {
                ajaxResult = AjaxResult.fail("没有查询到相关数据!");
            } else {
                List<ChannelServerProduct> groups = data.getGroups();
                // redisCache.setCacheObject(CACHE_PRODUCT_KEY,groups,6, TimeUnit.HOURS);
                Stream<ChannelServerProduct> stream = groups.stream();
                List<ChannelServerProduct> collect = null;
                if (!StringUtils.isEmpty(productCode)&&!productCode.equals("01")) {
                     collect = groups.stream().filter(t -> t.getProductGroupCode().equals(productCode)).collect(Collectors.toList());
                    if (!StringUtils.isEmpty(productName)) {
                        collect = collect.stream().filter(t -> t.getProductCName().contains(productName)).collect(Collectors.toList());
                    }
                }else {
                    collect = groups.stream().collect(Collectors.toList());
                    if (!StringUtils.isEmpty(productName)) {
                        collect = collect.stream().filter(t -> t.getProductCName().contains(productName)).collect(Collectors.toList());
                    }
                }
                ajaxResult = AjaxResult.success("操作成功!", collect);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取产品信息列表-异常:[{}]", e.getMessage());
            ajaxResult = AjaxResult.error("查询失败，请联系管理员");
        }
        // }
        return ajaxResult;
    }

    /**
     * 通过productCode获取产品配置信息
     * @param productCode
     * @return
     */
    @GetMapping("/selectByProductCode")
    public AjaxResult selectByProductCode(String productCode){
        return null;
    }

    /**
     * 删除配置
     * @param configCode
     */
    @GetMapping("/deleteProductConfig")
    public AjaxResult deleteProductConfig(Long configCode){
        try {
            sysProductConfigService.deleteProductConfig(configCode);
        }catch (Exception e){
            log.error("删除失败,[]"+e.getMessage());
            return AjaxResult.fail("删除失败");
        }
        return AjaxResult.success("删除成功");
    }

    /**
     * 通过员工号查询配置产品
     */
    @GetMapping("/selectByNum")
    public AjaxResult selectByNum(String saleNum){
        log.info("根据手机号查询配置产品接口开始,{}",saleNum);
        if(StringUtils.isEmpty(saleNum)){
            return new AjaxResult(HttpStatus.BAD_REQUEST,"请求参数不能为空");
        }
        try {
            List<ChannelServerProduct> list = new ArrayList<>();
            Set<Product> products = sysProductConfigService.selectByNum(saleNum);
            if (!products.isEmpty()) {
                Set<String> set = new HashSet<>();
                for (Product product : products) {
                    set.add(product.getProductCode());
                }
                ChannelServerData data = channelProductService.productListFromChannel("01");
                if (data == null) {
                    return AjaxResult.fail("没有查询到相关数据!");
                } else {
                    for (String productCode : set) {
                        List<ChannelServerProduct> groups = data.getGroups();
                        for (ChannelServerProduct group : groups) {
                            if (group.getProductCode().equals(productCode)) {
                                list.add(group);
                            }
                        }
                    }
                }
            }
            log.info("根据手机号查询配置产品接口end");
            return AjaxResult.success(list);
        } catch (Exception e) {
            log.error("查询失败,[]" + e.getMessage());
            return AjaxResult.error("查询失败,系统错误");
        }
    }
}
