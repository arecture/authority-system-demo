package com.manong.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.manong.entity.Department;
import com.manong.service.DepartmentService;
import com.manong.utils.Result;
import com.manong.vo.query.DepartmentQueryVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Resource
    private DepartmentService departmentService;

    /**
     * 查询部门列表
     * @param departmentQueryVo
     * @return
     */
    @GetMapping("/list")
    public Result list(DepartmentQueryVo departmentQueryVo){
//        调用查询部门列表方法
        List<Department> departmentList = departmentService.findDepartmentList(departmentQueryVo);
//        返回数据
        return Result.ok(departmentList);
    }

    /**
     * 查询上级部门列表
     * @return
     */
    @GetMapping("/parent/list")
    public Result getParentDepartment(){
//        调用查询上级部门列表方法
        List<Department> departmentList = departmentService.findParentDepartment();
//        返回数据
        return Result.ok(departmentList);
    }

    /**
     * 部门添加方法
     * @param department
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Department department){
        if(departmentService.save(department)){
            return Result.ok().message("部门添加成功");
        }
        return Result.error().message("部门添加失败");
    }

    /**
     * 部门修改方法
     * @param department
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody Department department){
        if(departmentService.updateById(department)){
            return Result.ok().message("部门修改成功");
        }
        return Result.error().message("部门修改失败");
    }

    /**
     * 查询某个部门下是否存在子部门
     * @param id
     * @return
     */
    @GetMapping("/check/{id}")
    public Result check(@PathVariable Long id){
//        判断数量是否大于0
        if (departmentService.hasChildrenOfDepartment(id)) {
            return Result.exist().message("该部门下存在子部门，无法删除");
        }
//        调用查询部门下是否有用户
        if (departmentService.hasUserOfDepartment(id)) {
            return Result.exist().message("该部门下存在用户，无法删除");
        }
        return Result.ok();
    }

    /**
     * 部门删除方法
     * @param id
     * @return 
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id){
        if(departmentService.removeById(id)){
            return Result.ok().message("部门删除成功");
        }
        return Result.error().message("部门删除失败");
    }
}

