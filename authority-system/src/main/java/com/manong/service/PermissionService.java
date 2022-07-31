package com.manong.service;

import com.manong.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manong.vo.query.PermissionQueryVo;

import java.util.List;

public interface PermissionService extends IService<Permission> {
    /**
     * 根据用户id查询权限菜单
     * @param userId
     * @return
     */
    List<Permission> findPermissionListByUserId(Long userId);

    /**
     * 查询菜单列表
     * @param permissionQueryVo
     * @return
     */
    List<Permission> findPermissionList(PermissionQueryVo permissionQueryVo);

    /**
     * 查询上级菜单列表
     * @return
     */
    List<Permission> findParentPermissionList();

    /**
     * 判断菜单是否有子菜单
     * @param id
     * @return
     */
    boolean hasChildrenOfPermission(Long id);
}
