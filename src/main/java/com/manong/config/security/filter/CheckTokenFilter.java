package com.manong.config.security.filter;

import com.manong.config.redis.RedisService;
import com.manong.config.security.exception.CustomerAuthenticationException;
import com.manong.config.security.handler.LoginFailureHandler;
import com.manong.config.security.service.CustomerUserDetailsService;
import com.manong.utils.JwtUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName CheckTokenFilter
 * @Description TODO
 * @Author 40706
 * @Date 2022/7/17 13:51
 * @Version
 **/
@Data
@Component
public class CheckTokenFilter extends OncePerRequestFilter {
//    声明登陆地址，登陆地址在配置里
    @Value("${request.login.url}")
    private String loginUrl;

    @Resource
    private RedisService redisService;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private CustomerUserDetailsService customerUserDetailsService;

    @Resource
    private LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            //        请求当前页面的url
            String url = request.getRequestURI();
//        判断是否是登录地址，除登陆地址以外要带token
            if(!url.equals(loginUrl)){
//            token认证
                this.validateToken(request);
            }
        }catch (AuthenticationException e){
//            验证失败
            loginFailureHandler.onAuthenticationFailure(request,response,e);
        }
//        如果是登陆页面，放行
        doFilter(request,response,filterChain);
    }

    /**
     * 验证token信息
     * @param request
     */
    private void validateToken(HttpServletRequest request) {
//            从前端提交获取token，有可能在头部，有可能传值
        String token = request.getHeader("token");
//        判断token是否在头部
        if (ObjectUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }
//        如果token不在头部也不再传值里，那就抛出异常
        if (ObjectUtils.isEmpty(token)) {
            throw new CustomerAuthenticationException("token不存在");
        }
//        判断redis里面是否存在token
        String tokenKey = "token_" + token;
        String redisToken = redisService.get(tokenKey);
//        如果token为空，token失效
        if(ObjectUtils.isEmpty(redisToken)){
            throw new CustomerAuthenticationException("token已过期");
        }
//        如果redis的token与生成token不一致，抛异常
        if(!token.equals(redisToken)){
            throw new CustomerAuthenticationException("token验证失败");
        }
//      如果token存在，就解析出用户名
        String username = jwtUtils.getUsernameFromToken(token);
        if (ObjectUtils.isEmpty(username)) {
            throw new CustomerAuthenticationException("token解析失败");
        }
//        获取用户信息
        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new CustomerAuthenticationException("token验证失败");
        }
//        创建用户身份验证对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
//        设置用户请求信息
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        将验证信息交给Spring Security上下文
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
