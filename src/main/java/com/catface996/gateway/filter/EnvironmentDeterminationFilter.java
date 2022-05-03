package com.catface996.gateway.filter;

import java.util.Map;

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

    private static final String ENV = "env";

    private static final String TOKEN = "token";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 从请求中获取是否有token参数
        String token = exchange.getRequest().getHeaders().getFirst(TOKEN);
        log.info("token:{}", token);

        // 根据 token 选择环境，此处是模拟，一般是调用用户的登录管理模块来做 token->userId 的转换
        if (StringUtils.isEmpty(token)){
            log.warn("无法获取 token");
            // 无法获取 token，通过这个过滤器，进入过滤链中的下一个过滤器
            return chain.filter(exchange);
        }
        String userId = userTokenConfig.get(token);
        if (StringUtils.isEmpty(userId)) {
            log.warn("根据 token 无法识别用户，token：{}", token);
            // 无法获取用户 ID，通过这个过滤器，进入过滤链中的下一个过滤器
            return chain.filter(exchange);
        }

        String env = userEnvConfig.get(userId);
        if (StringUtils.isEmpty(env)){
            // 决策的 env 为空，通过这个过滤器，进入过滤链中的下一个过滤器
            return chain.filter(exchange);
        }

        log.info("用户：{}，决策的环境为：{}", userId, env);
        ServerHttpRequest.Builder builder =  exchange.getRequest().mutate();
        builder.header(ENV,env);
        ServerHttpRequest request = builder.build();
        exchange.mutate().request(request).build();

        // 通过这个过滤器，进入过滤链中的下一个过滤器
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
