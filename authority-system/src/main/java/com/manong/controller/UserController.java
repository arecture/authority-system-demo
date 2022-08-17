package com.manong.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manong.entity.User;
import com.manong.service.UserService;
import com.manong.utils.Result;
import com.manong.vo.query.UserQueryVo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private PasswordEncoder passwordEncoder;
    /**
     * 查询user所有结果
     * @return
     */
    @GetMapping("/listAll")
    public Result listAll(){
        return Result.ok(userService.list());
    }

    /**
     * 查询用户列表
     * @return
     */
    @GetMapping("/list")
    public Result list(UserQueryVo userQueryVo){
//        创建分页对象
        IPage<User> page = new Page<>(userQueryVo.getPageNo(),userQueryVo.getPageSize());
//        调用分页查询方法
        userService.findUserListByPage(page,userQueryVo);
        return Result.ok(page);
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody User user){
        //查询用户
        User item = userService.findUserByUserName(user.getUsername());
//        判断对象是否为空，不为空则用户存在
        if(item != null){
            return Result.error().message("该用户名已被使用，请重新输入");
        }
//        密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        调用保存用户信息的方法
        if (userService.save(user)) {
            return Result.ok().message("用户添加成功");
        }
        return Result.error().message("用户添加失败");
    }
}

