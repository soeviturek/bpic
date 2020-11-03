package com.bpic.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bpic.common.core.domain.entity.TempCdeSales;
import com.bpic.system.domain.SysUserRole;
import com.bpic.system.domain.vo.TempCdeSalesVo;
import org.apache.ibatis.annotations.Param;
import com.bpic.common.core.domain.entity.SysUser;

/**
 * 用户表 数据层
 * 
 * @author ruoyi
 */
public interface SysUserMapper extends BaseMapper<TempCdeSales>
{
    /**
     * 根据条件分页查询用户列表
     * 
     * @param sysUser 用户信息
     * @return 用户信息集合信息
     */
    public List<TempCdeSalesVo> selectUserList(SysUser sysUser);

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
     * 修改用户头像
     * 
     * @param userName 用户名
     * @param avatar 头像地址
     * @return 结果
     */
    public int updateUserAvatar(@Param("userName") String userName, @Param("avatar") String avatar);

    /**
     * 重置用户密码
     * 
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    public int resetUserPwd(@Param("userName") String userName, @Param("password") String password);

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
     * 校验用户名称是否唯一
     * 
     * @param userName 用户名称
     * @return 结果
     */
    public int checkUserNameUnique(String userName);

    /**
     * 校验手机号码是否唯一
     *
     * @param phonenumber 手机号码
     * @return 结果
     */
    public SysUser checkPhoneUnique(String phonenumber);

    /**
     * 校验email是否唯一
     *
     * @param email 用户邮箱
     * @return 结果
     */
    public SysUser checkEmailUnique(String email);

    Long isAdmin(Long c_emp_cde);

    List<String> selectRoleKey(Long c_emp_cde);

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

    int bindRole(TempCdeSales user);

    List<SysUserRole> selectUserRole(String c_emp_cde);

    void deleteUserRoleByUser(String c_emp_cde);

    List<Long> getUserRole(TempCdeSales user);

    List<TempCdeSalesVo> selectByList(@Param("list")List<String> list);

    List<TempCdeSalesVo> selectByUserList(@Param("list")List<String> list);
}
