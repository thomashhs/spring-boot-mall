package com.imooc.mall.service;

import com.imooc.mall.model.dao.UserMapper;
import com.imooc.mall.model.pojo.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService{
    @Resource
    private UserMapper userMapper;

    @Override
    public User selectByUserId(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public User selectByUserName(String userName) {
        return userMapper.selectByUserName(userName);
    }
}
