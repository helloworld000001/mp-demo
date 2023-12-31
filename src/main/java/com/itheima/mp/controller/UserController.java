package com.itheima.mp.controller;

import cn.hutool.core.bean.BeanUtil;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @auther 陈彤琳
 * @Description $
 * 2023/11/29 16:08
 */
@Api("用户管理接口")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    // 这里不使用@Autowired自动注入，因为不是Spring推荐的方式
    // 推荐使用的构造器注入，如果我们加上final，配合@RequiredArgsConstructor就会在初始化时对必需的属性进行注入
    private final IUserService userService;

    @ApiOperation("新增用户接口")
    @PostMapping
    public void saveUser(@RequestBody UserFormDTO userDTO){
        // 1. 把前端传来的dto拷贝到po
        User user = BeanUtil.copyProperties(userDTO, User.class);

        // 2. 新增操作
        userService.save(user);
    }

    @ApiOperation("删除用户接口")
    @DeleteMapping("{id}")
    public void deleteUserById(@ApiParam("用户id") @PathVariable("id") Long id){
        userService.removeById(id);
    }

    // 查询用户同时，查询出用户对应的所有地址
    @ApiOperation("根据id查询用户接口")
    @GetMapping("{id}")
    public UserVO queryUserById(@ApiParam("用户id") @PathVariable("id") Long id){
        return userService.queryUserAndAddressById(id);
    }

    @ApiOperation("根据ids批量查询用户接口")
    @GetMapping
    public List<UserVO> queryUserByIds(@ApiParam("用户id集合") @RequestParam("ids") List<Long> ids){
        return userService.queryUserAndAddressByIds(ids);
    }

    @ApiOperation("扣减用户余额接口")
    @DeleteMapping("/{id}/deduction/{money}")
    public void deductMoneyById(
            @ApiParam("用户id") @PathVariable("id") Long id,
            @ApiParam("扣减金额") @PathVariable("money") Integer money){
        userService.deductBalance(id, money);
    }

    @ApiOperation("根据复杂条件查询用户接口")
    @GetMapping("/list")
    public List<UserVO> queryUsers(UserQuery query){
        // 1. 查询数据库得到用户PO
        List<User> users = userService.queryUsers(
                query.getName(), query.getStatus(), query.getMaxBalance(), query.getMinBalance());


        // 2. 将用户PO拷贝到VO返回给前端
        return BeanUtil.copyToList(users, UserVO.class);
    }
}
