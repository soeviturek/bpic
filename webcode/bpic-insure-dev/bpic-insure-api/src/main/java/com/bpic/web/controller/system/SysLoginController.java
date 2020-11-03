package com.bpic.web.controller.system;

import java.util.List;
import java.util.Set;

import com.bpic.common.core.domain.entity.TempCdeSales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.bpic.common.constant.Constants;
import com.bpic.common.core.domain.AjaxResult;
import com.bpic.common.core.domain.entity.SysMenu;
import com.bpic.common.core.domain.entity.SysUser;
import com.bpic.common.core.domain.model.LoginBody;
import com.bpic.common.core.domain.model.LoginUser;
import com.bpic.common.utils.ServletUtils;
import com.bpic.framework.web.service.SysLoginService;
import com.bpic.framework.web.service.SysPermissionService;
import com.bpic.framework.web.service.TokenService;
import com.bpic.system.service.ISysMenuService;

/**
 * 登录验证
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/web")
public class SysLoginController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    /**
     * 登录方法
     * 
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        TempCdeSales user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     * 
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        TempCdeSales user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getC_emp_cde());
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
