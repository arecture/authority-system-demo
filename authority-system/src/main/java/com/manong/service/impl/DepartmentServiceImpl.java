package com.manong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.manong.entity.Department;
import com.manong.dao.DepartmentMapper;
import com.manong.service.DepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manong.utils.DepartmentTree;
import com.manong.vo.query.DepartmentQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@Transactional
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    /**
     * 查询部门列表
     *
     * @param departmentQueryVo
     * @return
     */
    @Override
    public List<Department> findDepartmentList(DepartmentQueryVo departmentQueryVo) {
//        创建条件构造器对象
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
//        部门名称
        queryWrapper.like(!ObjectUtils.isEmpty(departmentQueryVo.getDepartmentName()),"department_name",departmentQueryVo.getDepartmentName());
//        排序
        queryWrapper.orderByAsc("order_num");
//        查询部门列表
        List<Department> departmentList = baseMapper.selectList(queryWrapper);
//        生成部门树
        List<Department> departmentTree = DepartmentTree.makeDepartmentTree(departmentList, 0L);
        return departmentTree;
    }

    /**
     * 查询上级部门
     *
     * @return
     */
    @Override
    public List<Department> findParentDepartment() {
//        创建条件构造器对象
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
//        排序
        queryWrapper.orderByAsc("order_num");
//        查询部门列表
        List<Department> departmentList = baseMapper.selectList(queryWrapper);
//        创建部门对象
        Department department = new Department();
        department.setId(0L);
        department.setDepartmentName("顶级部门");
        department.setPid(-1L);
//        添加到列表
        departmentList.add(department);
//        返回部门列表
        return departmentList;
    }
}
