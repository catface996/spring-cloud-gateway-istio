package com.catface996.gateway.controller;

import java.util.Map;

import javax.validation.Valid;

import com.catface996.gateway.controller.request.SetUserEnvironmentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by 大猫
 * @date 2022/5/3 11:45 catface996 出品
 */
@Slf4j
@RestController
public class UserEnvironmentController {

    private final Map<String, String> userEnvConfig;

    public UserEnvironmentController(Map<String, String> userEnvConfig) {
        this.userEnvConfig = userEnvConfig;
    }

    @PostMapping(value = "/setUserEnvironment")
    public String setUserEnvironment(@RequestBody @Valid SetUserEnvironmentRequest request) {
        userEnvConfig.put(request.getUserId(), request.getEnvironment());
        return "success";
    }
}
