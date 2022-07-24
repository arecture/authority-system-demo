package com.manong.utils;

import com.manong.entity.Department;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 生成部门树
 **/

public class DepartmentTree {
    /**
     * 生成部门树列表
     * @param departmentList 部门列表
     * @param pid            父级部门ID
     * @return
     */
    public static List<Department> makeDepartmentTree(List<Department> departmentList,long pid){
//        创建部门，保存部门信息
        List<Department> list = new ArrayList<>();
//        判断数据列表是否为空，不为空就使用，为空就创建
        Optional.ofNullable(departmentList).orElse(new ArrayList<Department>())
        .stream().filter(item -> item != null && item.getPid() == pid)
        .forEach(item -> {
//            创建部门对象
            Department department = new Department();
//            复制属性
            BeanUtils.copyProperties(item,department);
//            获取到每一个item的下级部门，递归生成部门树
            List<Department> children = makeDepartmentTree(departmentList, item.getId());
//            设置子部门
            department.setChildren(children);
//            将部门对象添加到部门集合中
            list.add(department);
        });
//        返回数据
        return list;
    }
}
