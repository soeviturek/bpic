package com.bpic.system.mapper;

import java.util.List;

import com.bpic.common.core.domain.entity.TdepartmentSales;
import com.bpic.system.domain.vo.DeptVo;
import org.apache.ibatis.annotations.Param;
import com.bpic.common.core.domain.entity.SysDept;
import org.springframework.stereotype.Repository;

/**
 * 部门管理 数据层
 * 
 * @author ruoyi
 */
@Repository
public interface SysDeptMapper
{
    /**
     * 查询部门管理数据
     * 
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<TdepartmentSales> selectDeptList(SysDept dept);

    /**
     * 根据角色ID查询部门树信息
     * 
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    public List<Integer> selectDeptListByRoleId(Long roleId);

    /**
     * 根据部门ID查询信息
     * 
     * @param deptId 部门ID
     * @return 部门信息
     */
    public TdepartmentSales selectDeptById(String deptId);

    /**
     * 根据ID查询所有子部门
     * 
     * @param deptId 部门ID
     * @return 部门列表
     */
    public List<SysDept> selectChildrenDeptById(Long deptId);

    /**
     * 根据ID查询所有子部门（正常状态）
     * 
     * @param deptId 部门ID
     * @return 子部门数
     */
    public int selectNormalChildrenDeptById(String deptId);

    /**
     * 是否存在子节点
     * 
     * @param deptId 部门ID
     * @return 结果
     */
    public int hasChildByDeptId(String deptId);

    /**
     * 查询部门是否存在用户
     * 
     * @param deptId 部门ID
     * @return 结果
     */
    public int checkDeptExistUser(String deptId);

    /**
     * 校验部门名称是否唯一
     * 
     * @param c_dpt_cnm 部门名称
     * @param c_snr_dpt 父部门ID
     * @return 结果
     */
    public TdepartmentSales checkDeptNameUnique(@Param("c_dpt_cnm") String c_dpt_cnm, @Param("c_snr_dpt") String c_snr_dpt);

    /**
     * 新增部门信息
     * 
     * @param dept 部门信息
     * @return 结果
     */
    public int insertDept(TdepartmentSales dept);

    /**
     * 修改部门信息
     * 
     * @param dept 部门信息
     * @return 结果
     */
    public int updateDept(TdepartmentSales dept);

    /**
     * 修改所在部门的父级部门状态
     * 
     * @param dept 部门
     */
    public void updateDeptStatus(SysDept dept);

    /**
     * 修改子元素关系
     * 
     * @param depts 子元素
     * @return 结果
     */
    public int updateDeptChildren(@Param("depts") List<SysDept> depts);

    /**
     * 删除部门管理信息
     * 
     * @param deptId 部门ID
     * @return 结果
     */
    public int deleteDeptById(String deptId);

    List<TdepartmentSales> selectOwnDeptList();

    List<String> selectDeptCodeList(String deptCode);

    List<DeptVo> selectChildDeptList(@Param("deptId") String deptId);
}
