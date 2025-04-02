package org.pets.demo.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class IpAddressFilter implements RequestFilter {

    private static final String LOCALHOST_IP = "127.0.0.1";

    @Override
    public boolean filter(ServerWebExchange exchange) {
        String ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        if (!LOCALHOST_IP.equals(ip)) {
            // логика для блокировки других IP-адресов ...
            return false;
        }
        return true;
    }
}