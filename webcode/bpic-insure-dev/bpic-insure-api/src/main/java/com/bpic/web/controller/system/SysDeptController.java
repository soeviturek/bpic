package com.bpic.web.controller.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.bpic.common.core.domain.entity.TdepartmentSales;
import com.bpic.common.core.domain.entity.TempCdeSales;
import com.bpic.common.core.domain.model.LoginUser;
import com.bpic.common.utils.ServletUtils;
import com.bpic.framework.web.service.TokenService;
import com.bpic.system.domain.vo.DeptVo;
import com.bpic.system.domain.vo.TreeVo;
import com.bpic.system.service.ISysUserService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bpic.common.annotation.Log;
import com.bpic.common.constant.UserConstants;
import com.bpic.common.core.controller.BaseController;
import com.bpic.common.core.domain.AjaxResult;
import com.bpic.common.core.domain.entity.SysDept;
import com.bpic.common.enums.BusinessType;
import com.bpic.common.utils.SecurityUtils;
import com.bpic.common.utils.StringUtils;
import com.bpic.system.service.ISysDeptService;

/**
 * 部门信息
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/web/system/dept")
public class SysDeptController extends BaseController
{
    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysUserService sysUserService;
    /**
     * 获取部门列表
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    public AjaxResult list(SysDept dept)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long admin = sysUserService.isAdmin(loginUser.getUser().getC_emp_cde());
        if(admin!=null) {
            dept.setRoleId(admin);
            List<TdepartmentSales> depts = deptService.selectDeptList(dept);
            return AjaxResult.success(depts);
        }else {
            return AjaxResult.error("此用户权限不足");
        }

    }

//    /**
//     * 查询部门列表（排除节点）
//     */
//    @PreAuthorize("@ss.hasPermi('system:dept:list')")
//    @GetMapping("/list/exclude/{deptId}")
//    public AjaxResult excludeChild(@PathVariable(value = "deptId", required = false) Long deptId)
//    {
//        List<TdepartmentSales> depts = deptService.selectDeptList(new SysDept());
//        Iterator<TdepartmentSales> it = depts.iterator();
//        while (it.hasNext())
//        {
//            TdepartmentSales d = (TdepartmentSales) it.next();
//            if (d.getDeptId().intValue() == deptId
//                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + ""))
//            {
//                it.remove();
//            }
//        }
//        return AjaxResult.success(depts);
//    }

    /**
     * 根据部门编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    public AjaxResult getInfo(@PathVariable String deptId)
    {
        return AjaxResult.success(deptService.selectDeptById(deptId));
    }

    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysDept dept)
    {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long admin = sysUserService.isAdmin(loginUser.getUser().getC_emp_cde());
        if(admin!=null){
            dept.setRoleId(admin);
            List<TdepartmentSales> depts = deptService.selectDeptList(dept);
            return AjaxResult.success(deptService.buildDeptTreeSelect(depts));
        }else {
            return AjaxResult.error("用户权限不足");
        }

    }

    /**
     * 加载对应角色部门列表树
     */
    @GetMapping(value = "/roleDeptTreeselect/{roleId}/{deptId}")
    public AjaxResult roleDeptTreeselect(@PathVariable("roleId") Long roleId, @PathVariable String deptId)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long admin = sysUserService.isAdmin(loginUser.getUser().getC_emp_cde());
        List<TdepartmentSales> depts=null;
        if(admin!=null){
            SysDept dept=new SysDept();
            dept.setRoleId(admin);
            depts = deptService.selectDeptList(dept);
        }
        List<DeptVo> deptVos = deptService.selectChildDeptList(deptId);
        List<TreeVo> treeVoList = new ArrayList<>();
        if(!deptVos.isEmpty()){
            for (DeptVo deptVo : deptVos) {
                TreeVo treeVo=new TreeVo();
                treeVo.setName(deptVo.getC_dpt_abr());
                treeVo.setValue(deptVo.getC_dpt_cde());
                treeVoList.add(treeVo);

            }
        }
        AjaxResult ajax = AjaxResult.success(treeVoList);
//        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
//        ajax.put("depts", deptService.buildDeptTreeSelect(depts));
        return ajax;
    }

    /**
     * 新增部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody TdepartmentSales dept)
    {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
        {
            return AjaxResult.error("新增部门'" + dept.getC_dpt_cnm() + "'失败，部门名称已存在");
        }
        List<TdepartmentSales> tdepartmentSales = deptService.selectOwnDeptList();
        if(!tdepartmentSales.isEmpty()){
            List<Long> list=new ArrayList<>();
            for (TdepartmentSales tdepartmentSale : tdepartmentSales) {
                list.add(Long.parseLong(tdepartmentSale.getC_dpt_cde()));
            }
            dept.setC_dpt_cde(String.valueOf(Collections.max(list)+1));
        }
        dept.setC_crt_cde(SecurityUtils.getUsername());
        return toAjax(deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody TdepartmentSales dept)
    {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
        {
            return AjaxResult.error("修改部门'" + dept.getC_dpt_cnm() + "'失败，部门名称已存在");
        }
        else if (dept.getC_snr_dpt().equals(dept.getC_dpt_cde()))
        {
            return AjaxResult.error("修改部门'" + dept.getC_dpt_cnm() + "'失败，上级部门不能是自己");
        }
        else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getC_state())
                && deptService.selectNormalChildrenDeptById(dept.getC_dpt_cde()) > 0)
        {
            return AjaxResult.error("该部门包含未停用的子部门！");
        }
        dept.setC_upd_cde(SecurityUtils.getUsername());
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public AjaxResult remove(@PathVariable String deptId)
    {
        if (deptService.hasChildByDeptId(deptId))
        {
            return AjaxResult.error("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId))
        {
            return AjaxResult.error("部门存在用户,不允许删除");
        }
        return toAjax(deptService.deleteDeptById(deptId));
    }
}
