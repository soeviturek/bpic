package com.bpic.system.service;

import java.util.List;
import com.bpic.common.core.domain.entity.SysUser;
import com.bpic.common.core.domain.entity.TdepartmentSales;
import com.bpic.common.core.domain.entity.TempCdeSales;
import com.bpic.system.domain.vo.TempCdeSalesVo;

/**
 * 用户 业务层
 * 
 * @author ruoyi
 */
public interface ISysUserService
{
    /**
     * 根据条件分页查询用户列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public List<TempCdeSalesVo> selectUserList(SysUser user);

    /**
     * 通过用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象信息
     */
    public TempCdeSales selectUserByUserName(String userName);

    /**
     * 通过用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    public TempCdeSalesVo selectUserById(Long userId);

    /**
     * 根据用户ID查询用户所属角色组
     * 
     * @param userName 用户名
     * @return 结果
     */
    public String selectUserRoleGroup(String userName);

    /**
     * 根据用户ID查询用户所属岗位组
     * 
     * @param userName 用户名
     * @return 结果
     */
    public String selectUserPostGroup(String userName);

    /**
     * 校验用户名称是否唯一
     * 
     * @param userName 用户名称
     * @return 结果
     */
    public String checkUserNameUnique(String userName);

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    public String checkPhoneUnique(TempCdeSales user);

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    public String checkEmailUnique(SysUser user);

    /**
     * 校验用户是否允许操作
     * 
     * @param user 用户信息
     */
    public void checkUserAllowed(TempCdeSales user);

    /**
     * 新增用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int insertUser(TempCdeSales user);

    /**
     * 修改用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int updateUser(TempCdeSales user);

    /**
     * 修改用户状态
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int updateUserStatus(TempCdeSales user);

    /**
     * 修改用户基本信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int updateUserProfile(TempCdeSales user);

    /**
     * 修改用户头像
     * 
     * @param userName 用户名
     * @param avatar 头像地址
     * @return 结果
     */
    public boolean updateUserAvatar(String userName, String avatar);

    /**
     * 重置用户密码
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int resetPwd(TempCdeSales user);

    /**
     * 重置用户密码
     * 
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    public int resetUserPwd(String userName, String password);

    /**
     * 通过用户ID删除用户
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteUserById(String userId);

    /**
     * 批量删除用户信息
     * 
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    public int deleteUserByIds(Long[] userIds);

    /**
     * 导入用户数据
     * 
     * @param userList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
   // public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName);

    /**
     * 查询角色id
     */
    public Long isAdmin(String c_emp_cde);

    /**
     * 判断是否为管理员
     */
    public Boolean selectRoleKey(String c_emp_cde);

    /**
     * 手机号登录校验
     */
    TempCdeSales selectUserByPhone(String cellphone);

    /**
     * 手机密码登录
     */

    TempCdeSales selectUserByPhoneAndPwd(TempCdeSales tempCdeSales);

    /**
     * 重设密码
     * @param tempCdeSales
     */
    int updatePwd(TempCdeSales tempCdeSales);

    int registerByCellphone(TempCdeSales tempCdeSales);

 	List<TempCdeSales> selectOwmUser();

     String getParentDeptName(List<TdepartmentSales> list, StringBuffer sb, String code);

    int bindRole(TempCdeSales user);

    List<Long> getUserRole(TempCdeSales user);

    List<TempCdeSalesVo> selectByList(List<String> list);

    List<TempCdeSalesVo> selectByUserList(List<String> list);
}
