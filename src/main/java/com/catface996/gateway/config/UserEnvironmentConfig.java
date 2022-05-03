package com.catface996.gateway.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author by 大猫
 * @date 2022/5/3 11:46 catface996 出品
 */
@Slf4j
@Configuration
public class UserEnvironmentConfig {

    @Bean("userEnvConfig")
    public Map<String, String> userEnvConfig() {
        Map<String, String> map = new ConcurrentHashMap<>();
        map.put("123", "prod");
        map.put("456", "gray");
        return map;
    }
}
