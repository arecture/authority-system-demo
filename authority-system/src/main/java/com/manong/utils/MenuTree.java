package com.manong.utils;

import com.manong.entity.Permission;
import com.manong.vo.RouterVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 菜单树工具类
 **/
public class MenuTree {
    /**
     * 生成路由
     * @param menuList  菜单列表
     * @param pid       父菜单ID
     * @return
     */
    public static List<RouterVo> makeRouter(List<Permission> menuList,Long pid){
//        创建集合保存路由信息
        List<RouterVo> routerVoList = new ArrayList<>();
//        判断路由列表是否为空,若空创建集合对象,不为空使用菜单列表
//        jdk8的Optional
        Optional.ofNullable(menuList).orElse(new ArrayList<Permission>())
//                筛选数据不为空和与菜单父id相同的数据
        .stream().filter(item -> item != null && Objects.equals(item.getParentId(), pid))
        .forEach(item -> {
//            创建路由对象
            RouterVo routerVo = new RouterVo();
            routerVo.setName(item.getName());  //路由名称
            routerVo.setPath(item.getPath());   //路由地址
//            判断是否是一级菜单
            if (item.getParentId() == 0L) {
                routerVo.setComponent("Layout");//一级菜单组件
                routerVo.setAlwaysShow(true);   //是否显示路由
            }else {
                routerVo.setComponent(item.getUrl());//从item里面获取
                routerVo.setAlwaysShow(false);      //折叠路由
            }
//            设置Meta信息
            routerVo.setMeta(routerVo.new Meta(item.getLabel(),item.getIcon(),item.getCode().split(",")));
//            递归生成路由
            List<RouterVo> children = makeRouter(menuList, item.getId());//子菜单
            routerVo.setChildren(children);//设置子路由
//            将路由信息添加到集合
            routerVoList.add(routerVo);
        });
        return routerVoList;
    }

    /**
     * 生成菜单
     * @param menuList 菜单列表
     * @param pid       父菜单ID
     * @return
     */
    public static List<Permission> makeMenuTree(List<Permission> menuList,Long pid){
        List<Permission> permissionList = new ArrayList<>();
//        判断路由列表是否为空,若空创建集合对象,不为空使用菜单列表
//        jdk8的Optional
        Optional.ofNullable(menuList).orElse(new ArrayList<Permission>())
//                筛选数据不为空和与菜单父id相同的数据
                .stream().filter(item -> item != null && Objects.equals(item.getParentId(), pid))
                .forEach(item ->{
//                    创建权限菜单
                    Permission permission = new Permission();
//                    将原有的的数值复制给菜单对象
                    BeanUtils.copyProperties(item,permission);
//                    获取每一个item对象的子菜单递归生成菜单树
                    List<Permission> children = makeMenuTree(menuList, item.getId());
//                    设置子菜单
                    permission.setChildren(children);
//                    把子菜单加入菜单列表
                    permissionList.add(permission);
                });
        return permissionList;
    }
}
