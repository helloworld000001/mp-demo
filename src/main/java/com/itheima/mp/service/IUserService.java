package com.itheima.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.UserVO;
import jdk.internal.org.objectweb.asm.tree.IincInsnNode;

import java.util.List;

/**
 * @auther 陈彤琳
 * @Description $
 * 2023/11/29 14:50
 */
public interface IUserService extends IService<User> {
    void deductBalance(Long id, Integer money);

    List<User> queryUsers(String name, Integer status, Integer maxBalance, Integer minBalance);

    UserVO queryUserAndAddressById(Long id);

    List<UserVO> queryUserAndAddressByIds(List<Long> ids);
}
