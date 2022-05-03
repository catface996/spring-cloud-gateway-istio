package com.catface996.gateway.controller.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author by 大猫
 * @date 2022/5/3 11:50 catface996 出品
 */
@Data
public class SetUserEnvironmentRequest {

    @NotBlank(message = "用户 ID 不能为空")
    private String userId;

    @NotBlank(message = "用户所属环境不能为空")
    private String environment;

}
