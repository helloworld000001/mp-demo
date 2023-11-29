package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.mp.domain.po.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    List<User> queryUserByIds(@Param("ids") List<Long> ids);

    void updateBalanceByIds(@Param("ew") QueryWrapper<User> wrapper, @Param("amount") int amount);

    // 查询条件where id = ？ 不是复杂条件，就不使用wrapper
    // 这里演示基于注解方式完成sql语句
    @Update("UPDATE user SET balance = balance - #{money} WHERE id = #{id}")
    void deductBalance(@Param("id") Long id, @Param("money") Integer money);
}
