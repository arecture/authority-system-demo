package com.manong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.manong.dao.UserMapper;
import com.manong.entity.Role;
import com.manong.dao.RoleMapper;
import com.manong.entity.User;
import com.manong.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manong.vo.query.RoleQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

@Service
@Transactional
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private UserMapper userMapper;
    /**
     * 查询角色列表
     * @param page
     * @param roleQueryVo
     * @return
     */
    @Override
    public IPage<Role> findRoleListByUserId(IPage<Role> page, RoleQueryVo roleQueryVo) {
//        创建条件构造器
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
//        条件查询
        queryWrapper.like(!ObjectUtils.isEmpty(roleQueryVo.getRoleName()),"role_name",roleQueryVo.getRoleName());
//        排序
        queryWrapper.orderByAsc("id");
//        根据用户ID查询用户信息
        User user = userMapper.selectById(roleQueryVo.getUserId());
//        判断是否为空和权限
        if (user != null && !ObjectUtils.isEmpty(user.getIsAdmin()) && user.getIsAdmin() != 1) {
//            用户不为空且权限不是管理员，只能查询自己创建的用户
            queryWrapper.eq("create_user",roleQueryVo.getUserId());
        }
        return baseMapper.selectPage(page, queryWrapper);
    }

    /**
     * 检查角色是否被使用
     * @param id
     * @return
     */
    @Override
    public boolean hashRoleCount(Long id) {
        return baseMapper.getRoleCountByRoleId(id) > 0;
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @Override
    public boolean deleteRoleById(Long id) {
//        删除角色权限关系
        baseMapper.deleteRolePermissionByRoleId(id);
//        删除角色
        return baseMapper.deleteById(id)>0;
    }
}
