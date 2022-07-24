package com.manong.controller;

import com.manong.config.redis.RedisService;
import com.manong.entity.Permission;
import com.manong.entity.User;
import com.manong.entity.UserInfo;
import com.manong.utils.JwtUtils;
import com.manong.utils.MenuTree;
import com.manong.utils.Result;
import com.manong.vo.RouterVo;
import com.manong.vo.TokenVo;
import io.jsonwebtoken.Jwts;
import lombok.val;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    /**
     * 查询用户信息
     * @return
     */
    @GetMapping("/getInfo")
    public Result getInfo(){
//        从Spring security上下文获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        判断是否是空
        if(authentication == null){
            return Result.error().message("用户查询失败");
        }
//        获取用户信息
        User user = (User) authentication.getPrincipal();
//        获取用户权限信息
        List<Permission> permissionList = user.getPermissionList();
//        获取权限编码
        Object[] roles = permissionList.stream().filter(Objects::nonNull).map(Permission::getCode).toArray();
//        创建用户信息
        UserInfo userInfo = new UserInfo(user.getId(),user.getNickName(),user.getAvatar(),null,roles);
        return Result.ok(userInfo).message("用户查询成功");
    }

    /**
     * 获取登录用户的菜单数据
     * @return
     */
    @GetMapping("/getMenuList")
    public Result getMenuList(){
//        从Spring security上下文获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        获取用户信息
        User user = (User) authentication.getPrincipal();
//        获取用户权限信息
        List<Permission> permissionList = user.getPermissionList();
//        筛选当前用户拥有的菜单数据
        List<Permission> collect = permissionList.stream()
//                2代表按钮,不筛选按钮
                .filter(item -> item != null && item.getType() != 2)
                .collect(Collectors.toList());
//        生成路由信息
        List<RouterVo> routerVoList = MenuTree.makeRouter(collect, 0L);
//        返回信息
        return Result.ok(routerVoList).message("菜单数据获取成功");
    }

    /**
     * 退出登录
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request, HttpServletResponse response){
//        获取token
        String token = request.getHeader("token");
//        如果头部中没有token，则从参数中获取
        if (ObjectUtils.isEmpty(token)) {
//            从参数中获取token
            token = request.getParameter("token");
        }
//        从Spring Security上下文获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        判断用户信息是否为空，如果不为空，清空用户信息
        if (authentication != null) {
//            清空用户信息
            new SecurityContextLogoutHandler().logout(request,response,authentication);
//            清楚缓存中的token
            redisService.del("token_" + token);
            return Result.ok().message("用户退出成功");
        }
        return Result.error().message("用户退出失败");
    }
}
