package com.bpic.web.controller.system;

import java.util.*;
import java.util.stream.Collectors;

import com.bpic.common.constant.HttpStatus;
import com.bpic.common.core.domain.entity.*;
import com.bpic.common.exception.CustomException;
import com.bpic.system.domain.vo.SysLoginUserVo;
import com.bpic.system.domain.vo.TempCdeSalesVo;
import com.bpic.system.service.*;
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
import org.springframework.web.multipart.MultipartFile;
import com.bpic.common.annotation.Log;
import com.bpic.common.constant.UserConstants;
import com.bpic.common.core.controller.BaseController;
import com.bpic.common.core.domain.AjaxResult;
import com.bpic.common.core.domain.model.LoginUser;
import com.bpic.common.core.page.TableDataInfo;
import com.bpic.common.enums.BusinessType;
import com.bpic.common.utils.SecurityUtils;
import com.bpic.common.utils.ServletUtils;
import com.bpic.common.utils.StringUtils;
import com.bpic.common.utils.poi.ExcelUtil;
import com.bpic.framework.web.service.TokenService;

import javax.annotation.Resource;

/**
 * 用户信息
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/web/system/user")
public class SysUserController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysDeptService deptService;

    @Resource
    SysLoginUserService sysLoginUserService;

    @Resource
    ISysDeptService iSysDeptService;

    @Resource
    ISysUserService iSysUserService;

    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long admin = userService.isAdmin(loginUser.getUser().getC_emp_cde());
        List<TdepartmentSales> depts=null;
        if(admin!=null){
            user.setRoleId(admin);
        }
        if(user.getDeptId()!=null&&user.getDeptId()!=0){
            List<String> stringList = deptService.selectDeptCodeList(user.getDeptId().toString());
            user.setList(stringList);
        }
        startPage();
        List<TempCdeSalesVo> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:user:export')")
    @GetMapping("/export")
    public AjaxResult export(SysUser user)
    {
        List<TempCdeSalesVo> list = userService.selectUserList(user);
        ExcelUtil<TempCdeSalesVo> util = new ExcelUtil<TempCdeSalesVo>(TempCdeSalesVo.class);
        return util.exportExcel(list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String operName = loginUser.getUsername();
        //String message = userService.importUser(userList, updateSupport, operName);
        String message="";
        return AjaxResult.success(message);
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate()
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.importTemplateExcel("用户数据");
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = { "/", "/{userId}" })
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId)
    {
        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        ajax.put("posts", postService.selectPostAll());
        if (StringUtils.isNotNull(userId))
        {
            ajax.put(AjaxResult.DATA_TAG, userService.selectUserById(userId));
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", roleService.selectRoleListByUserId(userId));
        }
        return ajax;
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody TempCdeSales user)
    {
//        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getC_emp_cnm())))
//        {
//            return AjaxResult.error("新增用户'" + user.getC_emp_cnm() + "'失败，登录账号已存在");
//        }
//        else
        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return AjaxResult.error("新增用户'" + user.getC_emp_cnm() + "'失败，手机号码已存在");
        }
        List<TempCdeSales> tempCdeSales = userService.selectOwmUser();
        if(!tempCdeSales.isEmpty()){
            List<Long> list=new ArrayList<>();
            for (TempCdeSales tempCdeSale : tempCdeSales) {
                list.add(Long.parseLong(tempCdeSale.getC_emp_cde()));
            }
            user.setC_emp_cde(String.valueOf(Collections.max(list)+1));
        }
        user.setC_crt_cde(SecurityUtils.getUsername());
        user.setC_passwd(SecurityUtils.encryptPassword(user.getC_passwd()));
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody TempCdeSales user)
    {
        userService.checkUserAllowed(user);
        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return AjaxResult.error("修改用户'" + user.getC_emp_cnm() + "'失败，手机号码已存在");
        }
//        else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
//        {
//            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
//        }
        user.setC_crt_cde(SecurityUtils.getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody TempCdeSales user)
    {
        userService.checkUserAllowed(user);
        user.setC_passwd(SecurityUtils.encryptPassword(user.getC_passwd()));
        user.setC_upd_cde(SecurityUtils.getUsername());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody TempCdeSales user)
    {
        userService.checkUserAllowed(user);
        user.setC_upd_cde(SecurityUtils.getUsername());
        return toAjax(userService.updateUserStatus(user));
    }

    /**
     * 用户绑定角色
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PostMapping("/bindRole")
    public AjaxResult bindRole(@RequestBody TempCdeSales user)
    {
        if (!userService.selectRoleKey(user.getCurrentUser()))
        {
            throw new CustomException("不允许操作超级管理员用户");
        }
        return toAjax(userService.bindRole(user));
    }

    /**
     * 回显用户对应角色
     */
    @PostMapping("/getUserRole")
    public AjaxResult getUserRole(@RequestBody TempCdeSales user)
    {
        if(user==null){
            return new AjaxResult(HttpStatus.BAD_REQUEST,"请求参数不能为空");
        }
        List<Long> userRole = userService.getUserRole(user);
        return AjaxResult.success(userRole);
    }


    /**
     * 查询所有下线列表
     * @param requestBody
     * @return
     */
    @PostMapping("/queryrelationList")
    public AjaxResult queryrelationList(@RequestBody HashMap<String, String> requestBody){
        logger.info("===========请求queryrelationList开始================");
        String cellphone = requestBody.get("cellphone");
        String user_name = requestBody.get("user_name");
        logger.info("========参数：cellphone：{}，user_name：{}=========",cellphone,user_name);
        logger.info("========开始查询下线信息=========");
        List<SysLoginUserVo> sysLoginUserVo = sysLoginUserService.queryrelationList(cellphone,user_name);
        List<SysLoginUserVo> newList = new ArrayList<>();
        for (SysLoginUserVo userVo : sysLoginUserVo) {
            List<TdepartmentSales> tdepartmentSales = iSysDeptService.selectOwnDeptList();
            logger.info("============员工所属部门查询===========");
            String parentDeptName = iSysUserService.getParentDeptName(tdepartmentSales, new StringBuffer(), userVo.getC_dpt_cde());
            userVo.setDept(parentDeptName);
            newList.add(userVo);
        }
        startPage();
        TableDataInfo dataTable = getDataTable(newList);
        return new AjaxResult(200,"操作成功",dataTable);
    }

    /**
     * 查询单个下线信息，传下线手机号
     * @param requestBody
     * @return
     */
    @PostMapping("/queryrelation")
    public AjaxResult queryrelation(@RequestBody HashMap<String, String> requestBody){
        String cellphone = requestBody.get("cellphone");
        if(StringUtils.isEmpty(cellphone)){
            return new AjaxResult(400,"查询失败","下线员工手机号不能为空");
        }
        SysLoginUserVo sysLoginUserVo = sysLoginUserService.selectByCellphone(cellphone);
        return new AjaxResult(200,"操作成功",sysLoginUserVo);
    }

    /**
     * 更改用户的下线 传上线员工员工号,下线手机号
     * 根据员工号查渤海表获取上线手机号，上线部门编码，上线名称，上线手机号
     * 更新下线的上线数据
     * @param requestBody
     * @return
     */
    @PostMapping("/updaterelation")
    public AjaxResult updaterelation(@RequestBody HashMap<String, String> requestBody){
        String cellphone = requestBody.get("cellphone");
        String c_emp_cde = requestBody.get("c_emp_cde");
        if(StringUtils.isEmpty(cellphone)){
            return new AjaxResult(400,"查询失败","下线员工手机号不能为空");
        }
        if(StringUtils.isEmpty(c_emp_cde)){
            return new AjaxResult(400,"查询失败","上线员工号不能为空");
        }
        TempCdeSalesVo tempCdeSalesVo = iSysUserService.selectUserById(Long.valueOf(c_emp_cde));
        if(tempCdeSalesVo != null){
            SysLoginUserVo sysLoginUserVo = new SysLoginUserVo();
            sysLoginUserVo.setC_tel_mob(cellphone);
            sysLoginUserVo.setC_emp_cde(c_emp_cde);
            sysLoginUserVo.setCphone(tempCdeSalesVo.getC_tel_mob());
            sysLoginUserVo.setC_dpt_cde(tempCdeSalesVo.getC_dpt_cde());
            sysLoginUserVo.setC_emp_cnm(tempCdeSalesVo.getC_emp_cnm());
            int i = sysLoginUserService.updateOnlineUser(sysLoginUserVo);
            return new AjaxResult(200,"更新成功",i);
        }
      return new AjaxResult(400,"更新失败","上线员工信息不存在");
    }
}
