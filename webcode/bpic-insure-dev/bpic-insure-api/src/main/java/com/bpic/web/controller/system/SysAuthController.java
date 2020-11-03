package com.bpic.web.controller.system;

import com.bpic.common.constant.HttpStatus;
import com.bpic.common.core.controller.BaseController;
import com.bpic.common.core.domain.AjaxResult;
import com.bpic.common.core.domain.entity.TdepartmentSales;
import com.bpic.common.core.page.TableDataInfo;
import com.bpic.common.utils.StringUtils;
import com.bpic.system.domain.AuditImage;
import com.bpic.system.domain.vo.AuthenticationVo;
import com.bpic.system.service.ISysAuthService;
import com.bpic.system.service.ISysDeptService;
import com.bpic.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 实名认证管理
 * @author cjj 2020/08/20
 */

@RestController
@RequestMapping("/web/system/auth")
public class SysAuthController extends BaseController {
    @Autowired
    private ISysAuthService sysAuthService;

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private ISysUserService sysUserService;

    private static final Logger logger = LoggerFactory.getLogger(SysAuthController.class);
    /**
     * 查询实名认证信息
     * @return List<AuthenticationVo>
     */
    @PostMapping("/select")
    public TableDataInfo select(@RequestBody AuthenticationVo authenticationVo){
        logger.info("请求实名认证查询接口start==========================,{}",authenticationVo);
        List<AuthenticationVo> authenticationVos =null;
        try {
            if(!StringUtils.isEmpty(authenticationVo.getDeptId())){
                List<String> list = sysDeptService.selectDeptCodeList(authenticationVo.getDeptId());
                list.add(authenticationVo.getDeptId());
                authenticationVo.setDeptList(list);
            }
            startPage();
            authenticationVos = sysAuthService.select(authenticationVo);
            List<TdepartmentSales> tdepartmentSales = sysDeptService.selectOwnDeptList();
            for (AuthenticationVo authentication : authenticationVos) {
                StringBuffer stringBuffer=new StringBuffer();
                String parentDeptName = sysUserService.getParentDeptName(tdepartmentSales, stringBuffer, authentication.getDeptId());
                authentication.setDeptName(parentDeptName);
            }
        }catch (Exception e){
            logger.error("请求实名认证接口错误=================,{}",e.getMessage());
            TableDataInfo tableDataInfo = new TableDataInfo();
            tableDataInfo.setCode(HttpStatus.ERROR);
            tableDataInfo.setMsg("系统错误");
            return tableDataInfo;
        }
        logger.info("请求实名认证查询接口end==========================,{}",authenticationVos);
        return getDataTable(authenticationVos);
    }

    /**
     * 请求审核
     * @return
     */
    @PostMapping("/audit")
    public AjaxResult audit(@RequestBody AuthenticationVo authenticationVo){
        logger.info("审核接口start======================,{}",authenticationVo);
        if(authenticationVo==null){
            return new AjaxResult(HttpStatus.BAD_REQUEST,"请求参数为空");
        }
        try {
            sysAuthService.audit(authenticationVo);
        }catch (Exception e){
            logger.error("审核接口出错========================,{}",e.getMessage());
        }
        logger.info("审核接口end==================");
        return AjaxResult.success();
    }

    /**
     * 实名认证查看
     */
    @PostMapping("/check")
    public AjaxResult check(@RequestBody Map<String,String> map){
        logger.info("实名认证查看接口start======================,{}",map);
        if(map.get("cTelMob")==null){
            return new AjaxResult(HttpStatus.BAD_REQUEST,"请求参数为空");
        }
        AuditImage auditImage = sysAuthService.check(map.get("cTelMob"));
        logger.info("实名认证查看接口end==================,{}",auditImage);
        return AjaxResult.success(auditImage);
    }
}
