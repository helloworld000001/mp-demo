package com.itheima.mp.service;

import com.itheima.mp.domain.po.User;
import com.itheima.mp.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @auther 陈彤琳
 * @Description $
 * 2023/11/29 14:55
 */
@SpringBootTest
class IUserServiceTest {

    @Autowired
    private IUserService userService;

    @Test
    void testSaveUser(){
        User user = new User();
        // user.setId(5L);
        user.setUsername("Lilei");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
        user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userService.save(user);
    }

    @Test
    void testQueryUser(){
        List<User> users = userService.listByIds(Arrays.asList(1L, 2L, 4L));
        users.forEach(System.out::println);
    }

}