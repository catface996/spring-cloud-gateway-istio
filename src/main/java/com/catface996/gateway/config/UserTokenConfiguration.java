package com.catface996.gateway.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author by 大猫
 * @date 2022/5/3 11:53 catface996 出品
 */
@Configuration
public class UserTokenConfiguration {

    @Bean("userTokenConfig")
    public Map<String, String> userTokenConfig() {
        Map<String, String> map = new ConcurrentHashMap<>();
        map.put("abc", "123");
        map.put("def", "456");
        map.put("ghi", "789");
        return map;
    }

}
