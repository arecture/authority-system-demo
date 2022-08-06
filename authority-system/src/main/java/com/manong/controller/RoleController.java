package com.manong.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manong.dto.RolePermissionDTO;
import com.manong.entity.Role;
import com.manong.service.PermissionService;
import com.manong.service.RoleService;
import com.manong.utils.Result;
import com.manong.vo.RolePermissionVo;
import com.manong.vo.query.RoleQueryVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Resource
    RoleService roleService;
    @Resource
    PermissionService permissionService;

    /**
     * 查询角色列表
     * @param roleQueryVo
     * @return
     */
    @GetMapping("/list")
    private Result list(RoleQueryVo roleQueryVo){
//        创建IPage分页查询对象
        IPage<Role> page = new Page<>(roleQueryVo.getPageNo(),roleQueryVo.getPageSize());
//        调用方法
        roleService.findRoleListByUserId(page,roleQueryVo);
        return  Result.ok(page);
    }

    /**
     * 添加角色
     * @param role
     * @return
     */
    @PostMapping("/add")
    private Result add(@RequestBody Role role){
        if (roleService.save(role)) {
            return Result.ok().message("角色添加成功");
        }
        return Result.error().message("角色添加失败");
    }


    /**
     * 修改角色
     * @param role
     * @return
     */
    @PutMapping("/update")
    private Result update(@RequestBody Role role){
        if (roleService.updateById(role)) {
            return Result.ok().message("角色修改成功");
        }
        return Result.error().message("角色修改失败");
    }

    @DeleteMapping("/delete/{id}")
    private Result delete(@PathVariable Long id){
//        调用删除方法
        if (roleService.deleteRoleById(id)) {
            return Result.ok().message("角色删除成功");
        }
        return Result.error().message("角色删除失败");
    }

    /**
     * 检查用户角色是否被使用
     * @param id
     * @return
     */
    @GetMapping("/check/{id}")
    private Result check(@PathVariable Long id){
//        调用查询角色是否被使用方法
        if (roleService.hashRoleCount(id)) {
            return Result.exist().message("该角色已分配给其他用户，无法删除");
        }
        return Result.ok();
    }

    /**
     * 分配权限-查询权限树数据
     *
     * @param userId
     * @param roleId
     * @return
     * */

    @GetMapping("/getAssignPermissionTree")
    public Result getAssignPermissionTree(Long userId, Long roleId) {
        //调用查询权限树数据的方法
        RolePermissionVo permissionTree = permissionService.findPermissionTree(userId, roleId);
        //返回数据
        return Result.ok(permissionTree);
    }

    /**
     * 分配权限-保存权限数据
     *
     * @param rolePermissionDTO
     * @return
     */
    @PostMapping("/saveRoleAssign")
    public Result saveRoleAssign(@RequestBody RolePermissionDTO rolePermissionDTO) {
        if (roleService.saveRolePermission(rolePermissionDTO.getRoleId(), rolePermissionDTO.getList())) {
            return Result.ok().message("权限分配成功");
        } else {
            return Result.error().message("权限分配失败");
        }
    }
}

