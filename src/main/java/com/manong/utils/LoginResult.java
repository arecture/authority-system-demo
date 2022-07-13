package com.manong.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录结果类
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResult {
    //用户编号
    private Long id;
    //状态码
    private int code;
    //token令牌
    private String token;
    //token过期时间
    private Long expireTime;
}
