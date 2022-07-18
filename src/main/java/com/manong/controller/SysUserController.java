package com.manong.controller;

import com.manong.config.redis.RedisService;
import com.manong.utils.JwtUtils;
import com.manong.utils.Result;
import com.manong.vo.TokenVo;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private RedisService redisService;
    /**
     * 刷新token
     * @param request
     * @return
     */
    @PostMapping("/refreshToken")
    public Result refreshToken(HttpServletRequest request){
//        从headers获取token
        String token = request.getHeader("token");
        if(!ObjectUtils.isEmpty(token)){
//            从请求参数获取token
            token = request.getParameter("token");
        }
//        从Spring security上下文获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        获取用户身份信息
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        定义变量保存新的token信息
        String newToken = "";
//        验证提交过来的token 信息是否合法
        if (jwtUtils.validateToken(token,userDetails)) {
//            生成新的token
            newToken = jwtUtils.refreshToken(token);

        }
//            获取新的token过期时间
        long expireTime = Jwts.parser()
                .setSigningKey(jwtUtils.getSecret())
                .parseClaimsJws(newToken.replace("jwt_", ""))
                .getBody().getExpiration().getTime();
//            清除旧的token
        String oldTokenKey = "token_" + token;
        redisService.del(oldTokenKey);
//            将新的token保存到redis缓存中
        String newTokenKey = "token_" + newToken;
        redisService.set(newTokenKey,newToken,jwtUtils.getExpiration()/1000);
//            创建tokenVo对象
        TokenVo tokenVo = new TokenVo(expireTime,newToken);
        return Result.ok(tokenVo).message("token刷新成功");
    }

}
