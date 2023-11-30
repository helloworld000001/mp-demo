package com.itheima.mp.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.enums.UserStatus;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @auther 陈彤琳
 * @Description $
 * 2023/11/29 14:51
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Override
    @Transactional // 事务
    public void deductBalance(Long id, Integer money) {
        // 1. 查询用户
        User user = getById(id);

        // 2. 校验用户状态
        if (user == null || user.getStatus() == UserStatus.FORZEN) {
            throw new RuntimeException("用户状态异常");
        }

        // 3. 校验余额是否充足
        if (user.getBalance() < money) {
            throw new RuntimeException("用户余额不足");
        }

        // 4. 扣减余额 update user set balance = balance - money where id =
        // baseMapper.deductBalance(id, money);
        // 使用lambda完成，扣减余额后如果为0就将status置为2（冻结
        int remainBalance = user.getBalance() - money;
        lambdaUpdate()
                .set(User::getBalance, remainBalance)
                .set(remainBalance == 0, User::getStatus, 2)
                .eq(User::getId, id)
                // 防止两条线程同时操作获取到的remainBalance相同，导致只扣了一次
                // 加乐观锁，在操作update之前先比较，数值对了才操作
                .eq(User::getBalance, user.getBalance())
                // 执行更新操作
                .update();

    }

    @Override
    public List<User> queryUsers(String name, Integer status, Integer maxBalance, Integer minBalance) {
        return lambdaQuery()
                .like(name != null, User::getUsername, name)
                .eq(status != null, User::getStatus, status)
                .le(maxBalance != null, User::getBalance, maxBalance)
                .ge(minBalance != null, User::getBalance, minBalance)
                .list();
    }

    @Override
    public UserVO queryUserAndAddressById(Long id) {
        // 1. 查询用户
        User user = getById(id);
        if (user == null || user.getStatus() == UserStatus.FORZEN) {
            throw new RuntimeException("用户状态异常");
        }

        // 2. 查询地址
        List<Address> addresses = Db.lambdaQuery(Address.class) // 要查询的类
                .eq(Address::getUserId, id) // Address中等于当前id的
                .list();// 拿到的是多个地址，使用的是list

        // 3. 封装VO
        // 3.1 User对象 -- UserVO
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);

        // 3.2 Address对象 -- AddressVO
        if (!CollUtil.isEmpty(addresses)) {
            List<AddressVO> addressesVO = BeanUtil.copyToList(addresses, AddressVO.class);
            userVO.setAddresses(addressesVO);
        }
        return userVO;
    }

    @Override
    public List<UserVO> queryUserAndAddressByIds(List<Long> ids) {
        // 1. 批量查询用户
        List<User> users = listByIds(ids);
        if (CollUtil.isEmpty(users)) {
            return Collections.emptyList();
        }

        // 2. 批量查询地址
        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        // 2.1 获取根据用户id获取到所有地址
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .in(Address::getUserId, userIds)
                .list();
        // 2.2 所有Address转VO返回
        List<AddressVO> addressVOList = BeanUtil.copyToList(addresses, AddressVO.class);
        // 2.3 对用户地址集合分组:一个用户是一组，有该用户对应的地址
        // 根据AddressVO::getUserId进行分组，获得的Map中key是AddressVO::getUserId,value是根据id搜索到的Address集合
        Map<Long, List<AddressVO>> addressMap = new HashMap<>(0);
        if (CollUtil.isNotEmpty(addressVOList)) {
            addressMap = addressVOList.stream().collect(Collectors.groupingBy(AddressVO::getUserId));
        }

        // 3. 转VO返回
        List<UserVO> list = new ArrayList<>(users.size());

        for (User user : users) {
            // 3.1 User -- UserVO
            UserVO vo = BeanUtil.copyProperties(user, UserVO.class);
            list.add(vo);

            vo.setAddresses(addressMap.get(user.getId()));
        }
        return list;
    }
}
