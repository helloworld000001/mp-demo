package com.itheima.mp.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        if (user == null || user.getStatus() == 2){
            throw new RuntimeException("用户状态异常");
        }

        // 3. 校验余额是否充足
        if (user.getBalance() < money){
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
}
