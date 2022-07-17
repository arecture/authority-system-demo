package com.manong.controller;

import com.manong.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName SysUserController
 * @Description TODOshToken
 * @Author 40706
 * @Date 2022/7/17 22:46
 * @Version
 **/
@RestController
@RequestMapping("/api/sysUser")
public class SysUserController {
    /**
     * 刷新token
     * @param request
     * @return
     */
    @PostMapping("/refreshToken")
    public Result refreshToken(HttpServletRequest request){
//        从headers获取token
        String token = request.getRequestURI();
    }

}
