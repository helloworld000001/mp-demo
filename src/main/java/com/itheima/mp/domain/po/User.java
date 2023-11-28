package com.itheima.mp.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("tb_user")
public class User {

    /**
     * 用户id
     */
    // 如果不指定类型是自增类型，自动加上的id使用雪花算法（很长一串），而不是id自增
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    // 当数据库字段与成员变量名不一致的时候，需要指定数据库字段名
    // @TableField("`username`")
    private String username;

    /**
     * 密码
     */
    // 当数据库不存在这个字段时，exist设置为false，这样在query等其他操作时不会加上该字段
    // @TableField(exist = false)
    private String password;

    /**
     * 注册手机号
     */
    private String phone;

    /**
     * 详细信息
     */
    private String info;

    /**
     * 使用状态（1正常 2冻结）
     */
    private Integer status;

    /**
     * 账户余额
     */
    private Integer balance;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
