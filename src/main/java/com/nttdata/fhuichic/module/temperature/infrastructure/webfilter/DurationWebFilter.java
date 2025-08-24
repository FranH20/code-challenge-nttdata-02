package com.nttdata.fhuichic.module.temperature.infrastructure.webfilter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class DurationWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        long startNanos = System.nanoTime();

        exchange.getResponse().beforeCommit(() -> {
            long durationMs = (System.nanoTime() - startNanos) / 1_000_000;
            exchange.getResponse().getHeaders().set("X-Duration", durationMs + "ms");
            return Mono.empty();
        });

        return chain.filter(exchange);
    }
}
