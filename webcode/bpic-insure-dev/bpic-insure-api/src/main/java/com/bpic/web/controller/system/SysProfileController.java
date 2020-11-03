package com.bpic.web.controller.system;

import java.io.IOException;

import com.bpic.common.core.domain.entity.TempCdeSales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.bpic.common.annotation.Log;
import com.bpic.common.config.BpicConfig;
import com.bpic.common.core.controller.BaseController;
import com.bpic.common.core.domain.AjaxResult;
import com.bpic.common.core.domain.entity.SysUser;
import com.bpic.common.core.domain.model.LoginUser;
import com.bpic.common.enums.BusinessType;
import com.bpic.common.utils.SecurityUtils;
import com.bpic.common.utils.ServletUtils;
import com.bpic.common.utils.file.FileUploadUtils;
import com.bpic.framework.web.service.TokenService;
import com.bpic.system.service.ISysUserService;

/**
 * 个人信息 业务处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/web/system/user/profile")
public class SysProfileController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    /**
     * 个人信息
     */
    @GetMapping
    public AjaxResult profile()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        TempCdeSales user = loginUser.getUser();
        AjaxResult ajax = AjaxResult.success(user);
        ajax.put("roleGroup", userService.selectUserRoleGroup(loginUser.getUsername()));
        ajax.put("postGroup", userService.selectUserPostGroup(loginUser.getUsername()));
        return ajax;
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@RequestBody TempCdeSales user)
    {
        if (userService.updateUserProfile(user) > 0)
        {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            // 更新缓存用户信息
            loginUser.getUser().setC_tel_mob(user.getC_tel_mob());
            loginUser.getUser().setC_sex(user.getC_sex());
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public AjaxResult updatePwd(String oldPassword, String newPassword)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.md5MatchesPassword(oldPassword, password))
        {
            return AjaxResult.error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.md5MatchesPassword(newPassword, password))
        {
            return AjaxResult.error("新密码不能与旧密码相同");
        }
        if (userService.resetUserPwd(userName, SecurityUtils.md5Password(newPassword)) > 0)
        {
            // 更新缓存用户密码
            loginUser.getUser().setC_passwd(SecurityUtils.md5Password(newPassword));
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error("修改密码异常，请联系管理员");
    }

//    /**
//     * 头像上传
//     */
//    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
//    @PostMapping("/avatar")
//    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws IOException
//    {
//        if (!file.isEmpty())
//        {
//            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
//            String avatar = FileUploadUtils.upload(BpicConfig.getAvatarPath(), file);
//            if (userService.updateUserAvatar(loginUser.getUsername(), avatar))
//            {
//                AjaxResult ajax = AjaxResult.success();
//                ajax.put("imgUrl", avatar);
//                // 更新缓存用户头像
//                tokenService.setLoginUser(loginUser);
//                return ajax;
//            }
//        }
//        return AjaxResult.error("上传图片异常，请联系管理员");
//    }
}