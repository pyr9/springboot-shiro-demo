package com.pyr.shiro.demo1.mapper;

import com.pyr.shiro.demo1.model.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    User findByUsername(@Param("username") String username);
}
