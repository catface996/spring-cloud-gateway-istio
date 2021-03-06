package com.catface996.gateway.filter;

import java.util.Map;

import com.catface996.gateway.config.ConfigConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author by 大猫
 * @date 2022/5/3 11:40 catface996 出品
 */
@Slf4j
@Component
public class EnvironmentDeterminationFilter implements GlobalFilter, Ordered {

    private final Map<String, String> userTokenConfig;

    private final Map<String, String> userEnvConfig;

    public EnvironmentDeterminationFilter(Map<String, String> userTokenConfig,
                                          Map<String, String> userEnvConfig) {
        this.userTokenConfig = userTokenConfig;
        this.userEnvConfig = userEnvConfig;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String userId = exchange.getAttribute(ConfigConst.USER_ID);
        if (StringUtils.isEmpty(userId)) {
            log.warn("决策请求链路所属环境时，无法获取当前请求上下文中的 userId");
            return chain.filter(exchange);
        }

        String env = userEnvConfig.get(userId);
        if (StringUtils.isEmpty(env)) {
            return chain.filter(exchange);
        }

        log.info("用户：{}，决策的环境为：{}", userId, env);
        ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
        builder.header(ConfigConst.ENV, env);
        ServerHttpRequest request = builder.build();
        exchange.mutate().request(request).build();

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 1;
    }

}
