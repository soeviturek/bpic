package com.bpic.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.bpic.common.core.domain.entity.TdepartmentSales;
import com.bpic.common.core.domain.entity.TempCdeSales;
import com.bpic.system.domain.SysPost;
import com.bpic.system.domain.SysUserPost;
import com.bpic.system.domain.SysUserRole;
import com.bpic.system.domain.vo.TempCdeSalesVo;
import com.bpic.system.mapper.*;
import com.bpic.system.service.ISysConfigService;
import com.bpic.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bpic.common.annotation.DataScope;
import com.bpic.common.constant.UserConstants;
import com.bpic.common.core.domain.entity.SysRole;
import com.bpic.common.core.domain.entity.SysUser;
import com.bpic.common.exception.CustomException;
import com.bpic.common.utils.SecurityUtils;
import com.bpic.common.utils.StringUtils;

/**
 * 用户 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysUserServiceImpl implements ISysUserService
{
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private SysDeptMapper sysDeptMapper;
    /**
     * 根据条件分页查询用户列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<TempCdeSalesVo> selectUserList(SysUser user)
    {
        List<TempCdeSalesVo> tempCdeSalesVoList = userMapper.selectUserList(user);
        List<TdepartmentSales> list = sysDeptMapper.selectOwnDeptList();
        for (TempCdeSalesVo tempCdeSalesVo : tempCdeSalesVoList) {
            StringBuffer stringBuffer=new StringBuffer();
            stringBuffer.append(tempCdeSalesVo.getC_dpt_abr());
            String parentDeptName = getParentDeptName(list, stringBuffer, tempCdeSalesVo.getC_snr_dpt());
            tempCdeSalesVo.setParentDeptCode(parentDeptName);
        }
        return tempCdeSalesVoList;
    }

    /**
     * 查询上级部门
     */
    public String getParentDeptName(List<TdepartmentSales> list,StringBuffer sb, String code){
        for (TdepartmentSales tdepartmentSales : list) {
            if(!tdepartmentSales.getC_dpt_cde().equals("00")){
                if(tdepartmentSales.getC_dpt_cde().equals(code)){
                    sb.append(tdepartmentSales.getC_dpt_abr()).append(",");
                    getParentDeptName(list,sb,tdepartmentSales.getC_snr_dpt());
                }
            }
        }
        return sb.toString();
    }

    @Override
    public int bindRole(TempCdeSales user) {
        List<SysUserRole> sysUserRoles = userMapper.selectUserRole(user.getC_emp_cde());
        if(!sysUserRoles.isEmpty()){
            userMapper.deleteUserRoleByUser(user.getC_emp_cde());
        }
        return userMapper.bindRole(user);
    }

    @Override
    public List<Long> getUserRole(TempCdeSales user) {
        return userMapper.getUserRole(user);
    }

    @Override
    public List<TempCdeSalesVo> selectByList(List<String> list) {
        return userMapper.selectByList(list);
    }

    @Override
    public List<TempCdeSalesVo> selectByUserList(List<String> list) {
        return userMapper.selectByUserList(list);
    }

    /**
     * 通过用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public TempCdeSales selectUserByUserName(String userName)
    {
        return userMapper.selectUserByUserName(userName);
    }

    /**
     * 通过用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public TempCdeSalesVo selectUserById(Long userId)
    {
        return userMapper.selectUserById(userId);
    }

    /**
     * 查询用户所属角色组
     * 
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName)
    {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysRole role : list)
        {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString()))
        {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 查询用户所属岗位组
     * 
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName)
    {
        List<SysPost> list = postMapper.selectPostsByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysPost post : list)
        {
            idsStr.append(post.getPostName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString()))
        {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 校验用户名称是否唯一
     * 
     * @param userName 用户名称
     * @return 结果
     */
    @Override
    public String checkUserNameUnique(String userName)
    {
        int count = userMapper.checkUserNameUnique(userName);
        if (count > 0)
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkPhoneUnique(TempCdeSales user)
    {
        Long userId = StringUtils.isNull(user.getC_emp_cde()) ? -1L : Long.parseLong(user.getC_emp_cde());
        SysUser info = userMapper.checkPhoneUnique(user.getC_tel_mob());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkEmailUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     * 
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(TempCdeSales user)
    {
        if (!selectRoleKey(user.getC_emp_cde()))
        {
            throw new CustomException("不允许操作超级管理员用户");
        }
    }

    /**
     * 新增保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(TempCdeSales user)
    {
        // 新增用户信息
        int rows = userMapper.insertUser(user);
        // 新增用户岗位关联
       // insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }

    /**
     * 修改保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUser(TempCdeSales user)
    {
        String userId = user.getC_emp_cde();
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        //insertUserPost(user);
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户状态
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(TempCdeSales user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户基本信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(TempCdeSales user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户头像
     * 
     * @param userName 用户名
     * @param avatar 头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar)
    {
        return userMapper.updateUserAvatar(userName, avatar) > 0;
    }

    /**
     * 重置用户密码
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(TempCdeSales user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 重置用户密码
     * 
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(String userName, String password)
    {
        return userMapper.resetUserPwd(userName, password);
    }

    /**
     * 新增用户角色信息
     * 
     * @param user 用户对象
     */
    public void insertUserRole(TempCdeSales user)
    {
        Long[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles))
        {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roles)
            {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(Long.parseLong(user.getC_emp_cde()));
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0)
            {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * 新增用户岗位信息
     * 
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user)
    {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotNull(posts))
        {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>();
            for (Long postId : posts)
            {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0)
            {
                userPostMapper.batchUserPost(list);
            }
        }
    }

    /**
     * 通过用户ID删除用户
     * 
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteUserById(String userId)
    {
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 删除用户与岗位表
       // userPostMapper.deleteUserPostByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     * 
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    public int deleteUserByIds(Long[] userIds)
    {
        for (Long userId : userIds)
        {
            TempCdeSales tempCdeSales=new TempCdeSales();
            tempCdeSales.setC_emp_cde(userId.toString());
            checkUserAllowed(tempCdeSales);
        }
        return userMapper.deleteUserByIds(userIds);
    }

    @Override
    public Long isAdmin(String c_emp_cde) {
        System.out.println(Long.parseLong(c_emp_cde));
        return userMapper.isAdmin(Long.parseLong(c_emp_cde));
    }

    @Override
    public Boolean selectRoleKey(String c_emp_cde) {
        List<String> list = userMapper.selectRoleKey(Long.parseLong(c_emp_cde));
        if(!list.isEmpty()){
            for (String s : list) {
                if(s.equals("admin")){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<TempCdeSales> selectOwmUser() {
        return userMapper.selectOwmUser();
    }
    @Override
    public TempCdeSales selectUserByPhone(String cellphone) {
        return userMapper.selectUserByPhone(cellphone);
    }

    @Override
    public TempCdeSales selectUserByPhoneAndPwd(TempCdeSales tempCdeSales) {
        return userMapper.selectUserByPhoneAndPwd(tempCdeSales);
    }

    @Override
    public int updatePwd(TempCdeSales tempCdeSales) {
        return userMapper.updatePwd(tempCdeSales);
    }

    @Override
    public int registerByCellphone(TempCdeSales tempCdeSales) {
        return userMapper.registerByCellphone(tempCdeSales);
    }

    /**
     * 导入用户数据
     * 
     * @param userList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
//    @Override
//    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName)
//    {
//        if (StringUtils.isNull(userList) || userList.size() == 0)
//        {
//            throw new CustomException("导入用户数据不能为空！");
//        }
//        int successNum = 0;
//        int failureNum = 0;
//        StringBuilder successMsg = new StringBuilder();
//        StringBuilder failureMsg = new StringBuilder();
//        String password = configService.selectConfigByKey("sys.user.initPassword");
//        for (SysUser user : userList)
//        {
//            try
//            {
//                // 验证是否存在这个用户
//                TempCdeSales u = userMapper.selectUserByUserName(user.getUserName());
//                if (StringUtils.isNull(u))
//                {
//                    user.setPassword(SecurityUtils.encryptPassword(password));
//                    user.setCreateBy(operName);
//                    this.insertUser(user);
//                    successNum++;
//                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
//                }
//                else if (isUpdateSupport)
//                {
//                    user.setUpdateBy(operName);
//                    this.updateUser(user);
//                    successNum++;
//                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
//                }
//                else
//                {
//                    failureNum++;
//                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
//                }
//            }
//            catch (Exception e)
//            {
//                failureNum++;
//                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
//                failureMsg.append(msg + e.getMessage());
//                log.error(msg, e);
//            }
//        }
//        if (failureNum > 0)
//        {
//            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
//            throw new CustomException(failureMsg.toString());
//        }
//        else
//        {
//            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
//        }
//        return successMsg.toString();
//    }
}
