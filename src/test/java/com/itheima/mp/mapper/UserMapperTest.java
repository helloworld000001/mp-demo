package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.itheima.mp.domain.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testInsert() {
        User user = new User();
        // user.setId(5L);
        user.setUsername("Ersanzi");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
        user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    @Test
    void testSelectById() {
        User user = userMapper.selectById(5L);
        System.out.println("user = " + user);
    }


    @Test
    void testQueryByIds() {
        // 由于是java8,List.of是高版本才能使用的，所以换掉
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1L, 2L, 3L, 4L));
        users.forEach(System.out::println);
    }

    @Test
    void testQueryByIdsWithProperties() {
        // 由于是java8,List.of是高版本才能使用的，所以换掉
        List<User> users = userMapper.queryUserByIds(Arrays.asList(1L, 2L, 3L, 4L));
        users.forEach(System.out::println);
    }

    @Test
    void testUpdateById() {
        User user = new User();
        user.setId(5L);
        user.setBalance(20000);
        userMapper.updateById(user);
    }

    @Test
    void testDeleteUser() {
        userMapper.deleteById(6L);
    }

    @Test
    void testQueryWrapper(){
        // 查询出名字带o，存款大于等于1000的人的id username info balance
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .select("id", "username", "info", "balance")
                .like("username", "o")
                .ge("balance", 1000);

        List<User> users = userMapper.selectList(wrapper);
        System.out.println(users);
    }

    @Test
    void testQueryLambdaWrapper(){
        // 使用Lambda取代字符串硬编码：推荐使用
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .select(User::getId, User::getUsername, User::getInfo, User::getBalance)
                .like(User::getUsername, "o")
                .ge(User::getBalance, 1000);

        List<User> users = userMapper.selectList(wrapper);
        System.out.println(users);
    }

    /**
     * 使用QueryWrapper的update操作
     */
    @Test
    void testUpdateByQueryWrapper(){
        // 更新用户名为Jack的用户余额是2000
        User user = new User();
        user.setBalance(2000);

        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .eq("username", "jack");

        // 这里传入的user是要更新的数据封装到一个对象中
        userMapper.update(user, wrapper);
    }

    @Test
    void testUpdateWrapper(){
        // 更新id为1，2，4的用户余额，都扣200
        List<Long> ids = Arrays.asList(1L, 2L, 4L);
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .setSql("balance = balance - 200")
                .in("id", ids);

        // 要更新的内容在wrapper写，实体就设置为null
        userMapper.update(null, wrapper);
    }

    @Test
    void testCustomSqlWrapper(){
        // 更新id为1，2，4的用户余额，都扣200
        List<Long> ids = Arrays.asList(1L, 2L, 4L);
        int amount = 200;

        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .in("id", ids);

        // 调用自定义方法
        userMapper.updateBalanceByIds(wrapper, amount);
    }
}