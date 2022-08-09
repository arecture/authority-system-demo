package com.manong.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.manong.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manong.vo.query.UserQueryVo;

public interface UserService extends IService<User> {
    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    User findUserByUserName(String username);

    /**
     * 分页查询用户列表
     * @param page
     * @param userQueryVo
     * @return
     */
    IPage<User> findUserListByPage(IPage<User> page, UserQueryVo userQueryVo);
}
