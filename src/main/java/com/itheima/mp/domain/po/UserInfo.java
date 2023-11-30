package com.itheima.mp.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther 陈彤琳
 * @Description $
 * 2023/12/1 3:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of") //生成一个名为of的构造函数：of(int arg1, String arg2)
public class UserInfo {
    private Integer age;
    private String intro;
    private String gender;
}
