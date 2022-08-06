package com.manong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.manong.dao.UserMapper;
import com.manong.entity.Permission;
import com.manong.dao.PermissionMapper;
import com.manong.entity.User;
import com.manong.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manong.utils.MenuTree;
import com.manong.vo.RolePermissionVo;
import com.manong.vo.query.PermissionQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
@Transactional
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Resource
    private UserMapper userMapper;
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
        permission.setLabel("顶级菜单");
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

    /**
     * 查询权限菜单树
     *
     * @param userId
     * @param roleId
     * @return
     */
    @Override
    public RolePermissionVo findPermissionTree(Long userId, Long roleId) {
//        1.查询当前用户信息
        User user = userMapper.selectById(userId);
//            创建集合保存权限
        List<Permission> list = null;
//        2.判断当前登录用户是否是管理员，若是，查出所有权限，反则根据当前用户id查询相对应的权限
        if (user != null && ObjectUtils.isEmpty(user.getIsAdmin()) && user.getIsAdmin() == 1) {
//            查询出所有权限菜单
            list = baseMapper.selectList(null);
        }else{
//            根据当前用户id查询相对应的权限
            list = baseMapper.findPermissionListByUserId(userId);
        }
//        3.将登录用户所拥有的权限封装成菜单树
        List<Permission> permissionList = MenuTree.makeMenuTree(list, 0L);
//        4.查询出要分配角色拥有的权限列表
        List<Permission> rolePermissions = baseMapper.findPermissionListByRoleId(roleId);
//        创建集合保存权限id
        List<Long> listIds = new ArrayList<>();
//        进行数据回显
        Optional.ofNullable(list).orElse(new ArrayList<>())
                .stream()
                .filter(Objects::nonNull)
                .forEach(item ->{
                    Optional.ofNullable(rolePermissions).orElse(new ArrayList<>())
                            .stream().filter(Objects::nonNull)
                            .forEach(obj ->{
//                                判断两者权限id是否一致，一致拥有该权限
                                if (item.getId().equals(obj.getId())) {
//                                    将权限id保存到集合中
                                    listIds.add(obj.getId());
                                    return;
                                }
                            });
                });
//        创建RolePermissionVo类对象
        RolePermissionVo rolePermissionVo = new RolePermissionVo(permissionList,listIds.toArray());
        return rolePermissionVo;
    }
}
