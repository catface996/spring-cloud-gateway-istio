package com.catface996.gateway.filter;

import java.util.Map;

import com.catface996.gateway.config.ConfigConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author by 大猫
 * @date 2022/5/4 12:15 catface996 出品
 */
@Slf4j
@Component
public class TokenUserIdFilter  implements GlobalFilter, Ordered {

    private final Map<String, String> userTokenConfig;

    private final Map<String, String> userEnvConfig;

    public TokenUserIdFilter(Map<String, String> userTokenConfig,
                                          Map<String, String> userEnvConfig) {
        this.userTokenConfig = userTokenConfig;
        this.userEnvConfig = userEnvConfig;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 从请求中获取是否有token参数
        String token = exchange.getRequest().getHeaders().getFirst(ConfigConst.TOKEN);
        log.info("receive request，token:{}", token);

        // 根据 token 选择环境，此处是模拟，一般是调用用户的登录管理模块来做 token->userId 的转换
        if (StringUtils.isEmpty(token)){
            log.warn("无法获取 token");
            return chain.filter(exchange);
        }

        String userId = userTokenConfig.get(token);
        if (StringUtils.isEmpty(userId)) {
            log.warn("根据 token 无法识别用户，token：{}", token);
            return chain.filter(exchange);
        }

        log.info("token：{}，对应的 userId：{}",token,userId);
        exchange.getAttributes().put(ConfigConst.USER_ID,userId);

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
