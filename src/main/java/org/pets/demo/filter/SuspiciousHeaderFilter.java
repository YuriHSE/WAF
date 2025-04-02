package org.pets.demo.filter;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class SuspiciousHeaderFilter implements RequestFilter {

    private static final String SUSPICIOUS_HEADER = "X-Suspicious-Header";

    @Override
    public boolean filter(ServerWebExchange exchange) {
        if (exchange.getRequest().getHeaders().containsKey(SUSPICIOUS_HEADER)) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return true;
        }
        return false;
    }
}