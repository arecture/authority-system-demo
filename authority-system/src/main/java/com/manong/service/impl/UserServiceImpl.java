package com.manong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.manong.entity.User;
import com.manong.dao.UserMapper;
import com.manong.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manong.vo.query.UserQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

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

    /**
     * 分页查询用户列表
     *
     * @param page
     * @param userQueryVo
     * @return
     */
    @Override
    public IPage<User> findUserListByPage(IPage<User> page, UserQueryVo userQueryVo) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        部门编号
        queryWrapper.eq(!ObjectUtils.isEmpty(userQueryVo.getDepartmentId()),"department_id",userQueryVo.getDepartmentId());
//        用户名
        queryWrapper.like(!ObjectUtils.isEmpty(userQueryVo.getUsername()),"username",userQueryVo.getUsername());
//        真实姓名
        queryWrapper.like(!ObjectUtils.isEmpty(userQueryVo.getRealName()),"real_name",userQueryVo.getRealName());
//        电话
        queryWrapper.like(!ObjectUtils.isEmpty(userQueryVo.getPhone()),"phone",userQueryVo.getPhone());

        return  baseMapper.selectPage(page,queryWrapper);
    }
}
