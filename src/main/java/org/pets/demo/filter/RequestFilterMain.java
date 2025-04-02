package org.pets.demo.filter;

import org.pets.demo.service.IpBlockService;
import org.pets.demo.service.TrafficAnomalyDetectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class RequestFilterMain implements WebFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestFilter.class);

    @Autowired
    private RequestFilterChain requestFilterChain;
    @Autowired
    private IpBlockService ipBlockService;
    @Autowired
    private TrafficAnomalyDetectionService anomalyDetectionService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String clientIp = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        String requestPath = exchange.getRequest().getPath().toString();

        logger.info("Request Path: {}", exchange.getRequest().getPath());
        logger.info("Request Headers: {}", exchange.getRequest().getHeaders());
        logger.info("Request IP: {}", exchange.getRequest().getRemoteAddress());


        if (ipBlockService.isBlocked(clientIp)) {
            logger.warn("IP {} is blocked, blocking request...", clientIp);
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return Mono.empty(); // Блокировка запроса
        }

        if (anomalyDetectionService.isAnomalous(clientIp, requestPath)) {
            logger.warn("🚨 Anomalous request detected! Blocking IP: {}", clientIp);
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        return requestFilterChain.filter(exchange, chain); // Используем цепочку фильтров
    }
}