package com.manong.controller;


import com.manong.entity.Permission;
import com.manong.service.PermissionService;
import com.manong.utils.Result;
import com.manong.vo.query.PermissionQueryVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/permission")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    /**
     * 查询菜单列表
     * @param permissionQueryVo
     * @return
     */
    @GetMapping("/list")
    public Result list(PermissionQueryVo permissionQueryVo){
//        调用查询菜单的方法
        List<Permission> permissionList = permissionService.findPermissionList(permissionQueryVo);
        return Result.ok(permissionList);
    }

    /**
     * 查询上级菜单列表
     * @return
     */
    @GetMapping("/parent/list")
    public Result getParentList(){
        List<Permission> permissionList = permissionService.findParentPermissionList();
        return Result.ok(permissionList);
    }

    /**
     * 添加菜单
     * @param permission
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Permission permission){
//        调用新增方法
        if (permissionService.save(permission)) {
            return Result.ok().message("菜单添加成功");
        }
        return Result.error().message("菜单添加失败");
    }

    /**
     * 修改菜单
     * @param permission
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody Permission permission){
        if (permissionService.updateById(permission)) {
            return Result.ok().message("菜单修改成功");
        }
        return Result.error().message("菜单修改失败");
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id){
        if (permissionService.removeById(id)) {
            return Result.ok().message("菜单删除成功");
        }
        return Result.error().message("菜单删除失败");
    }

    /**
     * 检查菜单是否有子菜单
     * @param id
     * @return
     */
    @GetMapping("/check/{id}")
    public Result check(@PathVariable Long id){
//        判断菜单是否有子菜单
        if (permissionService.hasChildrenOfPermission(id)) {
            return Result.exist().message("该菜单下有子菜单，无法删除");
        }
        return Result.ok();
    }
}

