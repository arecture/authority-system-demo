package com.manong.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manong.entity.User;
import com.manong.service.UserService;
import com.manong.utils.Result;
import com.manong.vo.query.UserQueryVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

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
}

