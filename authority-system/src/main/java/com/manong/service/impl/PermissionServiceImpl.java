package com.manong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.manong.entity.Permission;
import com.manong.dao.PermissionMapper;
import com.manong.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manong.utils.MenuTree;
import com.manong.vo.query.PermissionQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    /**
     * 根据用户id查询权限菜单
     *
     * @param userId
     * @return
     */
    @Override
    public List<Permission> findPermissionListByUserId(Long userId) {
        return baseMapper.findPermissionListByUserId(userId);
    }

    /**
     * 查询菜单列表
     *
     * @param permissionQueryVo
     * @return
     */
    @Override
    public List<Permission> findPermissionList(PermissionQueryVo permissionQueryVo) {
//        新建查询构造器
        QueryWrapper queryWrapper = new QueryWrapper();
//        排序
        queryWrapper.orderByAsc("order_num");
//        调用查询菜单方法
        List<Permission> permissionList = baseMapper.selectList(queryWrapper);
//        生成菜单树
        List<Permission> menuTree = MenuTree.makeMenuTree(permissionList,0L);
//        返回数据
        return menuTree;
    }

    /**
     * 查询上级菜单列表
     *
     * @return
     */
    @Override
    public List<Permission> findParentPermissionList() {
//        生成构造器
        QueryWrapper queryWrapper = new QueryWrapper();
//        type只能从0和1之间选择
        queryWrapper.in("type", Arrays.asList(0,1));
//        排序
        queryWrapper.orderByAsc("order_num");
//        查询菜单数据
        List<Permission> permissionList = baseMapper.selectList(queryWrapper);
//        构造顶级菜单信息，如果数据库中菜单表没有数据，选择上级菜单时显示顶级菜单
        Permission permission = new Permission();
        permission.setId(0L);
        permission.setParentId(-1L);
        permission.setName("顶级菜单");
        permissionList.add(permission);
//        生成菜单数据
        List<Permission> menuTree = MenuTree.makeMenuTree(permissionList,-1L);
//        返回数据
        return menuTree;
    }

    /**
     * 判断菜单是否有子菜单
     *
     * @param id
     * @return
     */
    @Override
    public boolean hasChildrenOfPermission(Long id) {
        //        生成构造器
        QueryWrapper queryWrapper = new QueryWrapper();
//        查询父级id
        queryWrapper.eq("parent_id",id);
//        判断数量是否大于零，大于零表示存在
        if (baseMapper.selectCount(queryWrapper) > 0) {
            return true;
        }
        return false;
    }
}
