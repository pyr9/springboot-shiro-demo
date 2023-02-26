package com.pyr.shiro.demo1.service;

import com.pyr.shiro.demo1.mapper.UserMapper;
import com.pyr.shiro.demo1.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }
}
