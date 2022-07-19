package com.imooc.mall.service;

import com.imooc.mall.model.pojo.User;

public interface UserService {
    public User selectByUserId(Integer id);

    public User selectByUserName(String userName);
}
