package org.pets.demo.filter;

import org.springframework.web.server.ServerWebExchange;

public interface RequestFilter {
    boolean filter(ServerWebExchange exchange);
}