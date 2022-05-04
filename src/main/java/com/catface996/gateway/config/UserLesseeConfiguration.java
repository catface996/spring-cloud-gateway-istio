package com.catface996.gateway.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author by 大猫
 * @date 2022/5/4 12:12 catface996 出品
 */
@Configuration
public class UserLesseeConfiguration {

    @Bean("userLesseeConfig")
    public Map<String, String> userLesseeConfig() {
        Map<String, String> map = new ConcurrentHashMap<>();
        map.put("123", "L1");
        map.put("456", "L2");
        map.put("789", "L3");
        return map;
    }
}
