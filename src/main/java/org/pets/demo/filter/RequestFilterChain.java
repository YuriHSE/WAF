package org.pets.demo.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class RequestFilterChain implements WebFilter {

    @Autowired
    private List<RequestFilter> filters;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        for (RequestFilter filter : filters) {
            if (filter.filter(exchange)) {
                return Mono.empty();
            }
        }

        return chain.filter(exchange);
    }
}
