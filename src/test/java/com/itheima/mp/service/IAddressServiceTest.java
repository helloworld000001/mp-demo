package com.itheima.mp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @auther 陈彤琳
 * @Description $
 * 2023/12/1 2:20
 */
@SpringBootTest
class IAddressServiceTest {
    @Autowired
    protected IAddressService addressService;

    @Test
    void testLogicDelete() {
        // 1. 删除
        addressService.removeById(59L);

        // 2. 查询
        System.out.println(addressService.getById(59L));
    }

}