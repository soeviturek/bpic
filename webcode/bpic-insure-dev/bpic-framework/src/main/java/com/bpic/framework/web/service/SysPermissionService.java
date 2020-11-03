package com.bpic.framework.web.service;

import java.util.HashSet;
import java.util.Set;

import com.bpic.common.core.domain.entity.TempCdeSales;
import com.bpic.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bpic.common.core.domain.entity.SysUser;
import com.bpic.system.service.ISysMenuService;
import com.bpic.system.service.ISysRoleService;

/**
 * 用户权限处理
 * 
 * @author ruoyi
 */
@Component
public class SysPermissionService
{
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysUserService userService;

    /**
     * 获取角色数据权限
     * 
     * @param user 用户信息
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(TempCdeSales user)
    {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (userService.selectRoleKey(user.getC_emp_cde()))
        {
            roles.add("admin");
        }
        else
        {
            roles.addAll(roleService.selectRolePermissionByUserId(user.getC_emp_cde()));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     * 
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(TempCdeSales user)
    {
        Set<String> perms = new HashSet<String>();
        // 管理员拥有所有权限
        if (userService.selectRoleKey(user.getC_emp_cde()))
        {
            perms.add("*:*:*");
        }
        else
        {
            perms.addAll(menuService.selectMenuPermsByUserId(user.getC_emp_cde()));
        }
        return perms;
    }
}
