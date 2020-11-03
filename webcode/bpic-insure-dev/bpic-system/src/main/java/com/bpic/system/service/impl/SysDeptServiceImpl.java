package com.bpic.system.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.bpic.common.core.domain.entity.TdepartmentSales;
import com.bpic.system.domain.vo.DeptVo;
import com.bpic.system.mapper.SysDeptMapper;
import com.bpic.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bpic.common.annotation.DataScope;
import com.bpic.common.constant.UserConstants;
import com.bpic.common.core.domain.TreeSelect;
import com.bpic.common.core.domain.entity.SysDept;
import com.bpic.common.exception.CustomException;
import com.bpic.common.utils.StringUtils;

/**
 * 部门管理 服务实现
 * 
 * @author ruoyi
 */
@Service
public class SysDeptServiceImpl implements ISysDeptService
{
    @Autowired
    private SysDeptMapper deptMapper;

    /**
     * 查询部门管理数据
     * 
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<TdepartmentSales> selectDeptList(SysDept dept)
    {
        return deptMapper.selectDeptList(dept);
    }

    @Override
    public List<String> selectDeptCodeList(String deptCode) {
        List<TdepartmentSales> list = deptMapper.selectOwnDeptList();
        List<Map> maps = selectByParentId(list, deptCode);
        List<String> arrayList=new ArrayList<>();
        List<String> childrenList=null;
        List<String> lists=new ArrayList<>();
        for (Map map : maps) {
            arrayList.add(map.get("deptCode").toString());
            childrenList = getDeptCode((List<Map>) map.get("children"),lists);
            arrayList.addAll(childrenList);
        }
        arrayList.add(deptCode);
        return arrayList;
    }

    private List<String> getDeptCode(List<Map> list,List<String> lists){
        for (Map map : list) {
            lists.add(map.get("deptCode").toString());
            List<Map> children = (List<Map>) map.get("children");
            if(children!=null){
                getDeptCode(children,lists);
            }
        }
        return lists;
    }

    private List<Map> selectByParentId(List<TdepartmentSales> list, String deptCode) {
        List<Map> arrayList=new ArrayList<>();
        for (TdepartmentSales tdepartmentSales : list) {
            if(StringUtils.isNotEmpty(tdepartmentSales.getC_snr_dpt())&&tdepartmentSales.getC_snr_dpt().equals(deptCode)){
                Map map=new HashMap();
                map.put("deptCode",tdepartmentSales.getC_dpt_cde());
                map.put("children",selectByParentId(list,tdepartmentSales.getC_dpt_cde()));
                arrayList.add(map);
            }
        }
        return arrayList;
    }

    /**
     * 构建前端所需要树结构
     * 
     * @param depts 部门列表
     * @return 树结构列表
     */
    @Override
    public List<TdepartmentSales> buildDeptTree(List<TdepartmentSales> depts)
    {
        List<TdepartmentSales> returnList = new ArrayList<TdepartmentSales>();
        List<String> tempList = new ArrayList<String>();
        for (TdepartmentSales dept : depts)
        {
            tempList.add(dept.getC_dpt_cde());
        }
        for (Iterator<TdepartmentSales> iterator = depts.iterator(); iterator.hasNext();)
        {
            TdepartmentSales dept = (TdepartmentSales) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getC_snr_dpt()))
            {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     * 
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<TdepartmentSales> depts)
    {
        List<TdepartmentSales> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据角色ID查询部门树信息
     * 
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    @Override
    public List<Integer> selectDeptListByRoleId(Long roleId)
    {
        return deptMapper.selectDeptListByRoleId(roleId);
    }

    /**
     * 根据部门ID查询信息
     * 
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Override
    public TdepartmentSales selectDeptById(String deptId)
    {
        return deptMapper.selectDeptById(deptId);
    }

    /**
     * 根据ID查询所有子部门（正常状态）
     * 
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Override
    public int selectNormalChildrenDeptById(String deptId)
    {
        return deptMapper.selectNormalChildrenDeptById(deptId);
    }

    /**
     * 是否存在子节点
     * 
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(String deptId)
    {
        int result = deptMapper.hasChildByDeptId(deptId);
        return result > 0 ? true : false;
    }

    /**
     * 查询部门是否存在用户
     * 
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(String deptId)
    {
        int result = deptMapper.checkDeptExistUser(deptId);
        return result > 0 ? true : false;
    }

    /**
     * 校验部门名称是否唯一
     * 
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public String checkDeptNameUnique(TdepartmentSales dept)
    {
        String deptId = StringUtils.isNull(dept.getC_dpt_cde()) ? "" : dept.getC_dpt_cde();
        TdepartmentSales info = deptMapper.checkDeptNameUnique(dept.getC_dpt_cnm(), dept.getC_snr_dpt());
        if (StringUtils.isNotNull(info) && !info.getC_dpt_cde().equals(deptId))
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 新增保存部门信息
     * 
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int insertDept(TdepartmentSales dept)
    {
        TdepartmentSales info = deptMapper.selectDeptById(dept.getC_snr_dpt());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getC_state()))
        {
            throw new CustomException("部门停用，不允许新增");
        }
        return deptMapper.insertDept(dept);
    }

    /**
     * 修改保存部门信息
     * 
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int updateDept(TdepartmentSales dept)
    {
        TdepartmentSales newParentDept = deptMapper.selectDeptById(dept.getC_snr_dpt());
        TdepartmentSales oldDept = deptMapper.selectDeptById(dept.getC_dpt_cde());
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept))
        {
            //String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
           // String oldAncestors = oldDept.getAncestors();
            //dept.setAncestors(newAncestors);
           // updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
        }
        int result = deptMapper.updateDept(dept);
//        if (UserConstants.DEPT_NORMAL.equals(dept.getC_state()))
//        {
//            // 如果该部门是启用状态，则启用该部门的所有上级部门
//            updateParentDeptStatus(dept);
//        }
        return result;
    }

    /**
     * 修改该部门的父级部门状态
     * 
     * @param dept 当前部门
     */
    private void updateParentDeptStatus(SysDept dept)
    {
        String updateBy = dept.getUpdateBy();
       // dept = deptMapper.selectDeptById(dept.getDeptId());
        dept.setUpdateBy(updateBy);
        deptMapper.updateDeptStatus(dept);
    }

    /**
     * 修改子元素关系
     * 
     * @param deptId 被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors)
    {
        List<SysDept> children = deptMapper.selectChildrenDeptById(deptId);
        for (SysDept child : children)
        {
            child.setAncestors(child.getAncestors().replace(oldAncestors, newAncestors));
        }
        if (children.size() > 0)
        {
            deptMapper.updateDeptChildren(children);
        }
    }

    /**
     * 删除部门管理信息
     * 
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public int deleteDeptById(String deptId)
    {
        return deptMapper.deleteDeptById(deptId);
    }

    @Override
    public List<TdepartmentSales> selectOwnDeptList() {
        return deptMapper.selectOwnDeptList();
    }

    @Override
    public List<DeptVo> selectChildDeptList(String deptId) {
        return deptMapper.selectChildDeptList(deptId);
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<TdepartmentSales> list, TdepartmentSales t)
    {
        // 得到子节点列表
        List<TdepartmentSales> childList = getChildList(list, t);
        t.setChildren(childList);
        for (TdepartmentSales tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                // 判断是否有子节点
                Iterator<TdepartmentSales> it = childList.iterator();
                while (it.hasNext())
                {
                    TdepartmentSales n = (TdepartmentSales) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<TdepartmentSales>  getChildList(List<TdepartmentSales> list, TdepartmentSales t)
    {
        List<TdepartmentSales> tlist = new ArrayList<TdepartmentSales>();
        Iterator<TdepartmentSales> it = list.iterator();
        while (it.hasNext())
        {
            TdepartmentSales n = (TdepartmentSales) it.next();
            if (StringUtils.isNotNull(n.getC_snr_dpt()) && n.getC_snr_dpt().equals(t.getC_dpt_cde()))
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<TdepartmentSales> list, TdepartmentSales t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }
}
