package com.pyr.shiro.demo1.service;


import com.pyr.shiro.demo1.model.User;

public interface UserService {

    User findByUsername(String username);
}
