package com.manong.service;

import com.manong.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {
    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    User findUserByUserName(String username);
}
