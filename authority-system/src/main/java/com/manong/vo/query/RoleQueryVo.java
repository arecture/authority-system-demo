package com.manong.vo.query;

import com.manong.entity.Role;
import lombok.Data;

/**
 *
 **/

@Data
public class RoleQueryVo extends Role {
//    当前页码
    private Long pageNo = 1L;
//    每页显示数据多少
    private Long pageSize = 10L;
//    用户ID
    private Long userId;
}
