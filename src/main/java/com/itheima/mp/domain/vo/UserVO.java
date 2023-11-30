package com.itheima.mp.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 查询时的VO,对于pwd，time等字段查询时不需要所以没有
 */
@Data
@ApiModel(description = "用户VO实体")
public class UserVO {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("详细信息")
    private String info;

    @ApiModelProperty("使用状态（1正常 2冻结）")
    private Integer status;

    @ApiModelProperty("账户余额")
    private Integer balance;

    // VO用于返回给前端的实体:因为查询用户同时返回用户的所有地址，所以要把地址封装上
    @ApiModelProperty("用户的收获地址")
    private List<AddressVO> addresses;
}
