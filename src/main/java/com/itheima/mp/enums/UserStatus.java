package com.itheima.mp.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @auther 陈彤琳
 * @Description $
 * 2023/12/1 3:12
 */
@Getter
public enum UserStatus {
    // 默认返回到前端的是定义的字段名（NORMAL等，想要指定返回的值就在那个值上加@JsonValue注解
    NORMAL(1, "正常"),
    FORZEN(2, "冻结"),
    ;
    @EnumValue
    @JsonValue // 加在value上，返回到前端就是value属性值
    private final int value;
    private final String desc;

    UserStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
