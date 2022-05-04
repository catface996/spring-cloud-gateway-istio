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
 * @date 2022/5/4 12:29 catface996 出品
 */
@Slf4j
@Component
public class LesseeDeterminationFilter implements GlobalFilter, Ordered {

    private final Map<String, String> userEnvConfig;

    private final Map<String, String> userLesseeConfig;

    public LesseeDeterminationFilter(Map<String, String> userEnvConfig,
                                     Map<String, String> userLesseeConfig) {
        this.userEnvConfig = userEnvConfig;
        this.userLesseeConfig = userLesseeConfig;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String userId = exchange.getAttribute(ConfigConst.USER_ID);
        if (StringUtils.isEmpty(userId)){
            log.warn("决策请求链路所属租户时，无法获取当前请求上下文中的 userId");
            return chain.filter(exchange);
        }

        String lessee = userLesseeConfig.get(userId);
        if (StringUtils.isEmpty(lessee)) {
            return chain.filter(exchange);
        }

        log.info("用户：{}，决策的租户为：{}", userId, lessee);
        ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
        builder.header(ConfigConst.LESSEE, lessee);
        ServerHttpRequest request = builder.build();
        exchange.mutate().request(request).build();

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
