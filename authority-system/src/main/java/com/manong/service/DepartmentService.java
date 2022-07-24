package com.manong.service;

import com.manong.entity.Department;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manong.vo.query.DepartmentQueryVo;

import java.util.List;

public interface DepartmentService extends IService<Department> {
    /**
     * 查询部门列表
     * @param departmentQueryVo
     * @return
     */
    List<Department> findDepartmentList(DepartmentQueryVo departmentQueryVo);

    /**
     * 查询上级部门
     * @return
     */
    List<Department> findParentDepartment();
}
