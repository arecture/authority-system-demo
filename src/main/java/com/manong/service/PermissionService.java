package com.manong.service;

import com.manong.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface PermissionService extends IService<Permission> {
    /**
     * 根据用户id查询权限菜单
     * @param userId
     * @return
     */
    List<Permission> findPermissionListByUserId(Long userId);
}
