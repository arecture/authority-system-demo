package com.manong.dao;

import com.manong.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 根据用户id查询权限菜单
     * @param userId
     * @return
     */
    List<Permission> findPermissionListByUserId(Long userId);
}
