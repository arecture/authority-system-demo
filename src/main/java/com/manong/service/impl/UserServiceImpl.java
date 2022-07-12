package com.manong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.manong.entity.User;
import com.manong.dao.UserMapper;
import com.manong.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 根据用户名查询用户信息
     */
    @Override
    public User findUserByUserName(String username) {
        /*
          创建条件构造器对象
          QueryWrapper类文档:https://baomidou.com/pages/10c804/#querywrapper
          eq方法文档：https://baomidou.com/pages/10c804/#eq
         */
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return baseMapper.selectOne(queryWrapper);
    }
}
