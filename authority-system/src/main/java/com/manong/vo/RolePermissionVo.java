package com.manong.vo;

import com.manong.entity.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色权限菜单数据回显Vo类
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionVo {

    /**
     * 菜单数据
     */
    private List<Permission> permissionList = new ArrayList<>();

    /**
     * 该角色拥有的菜单权限
     */
    private Object[] checkList;
}
