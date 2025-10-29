package com.caro.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register the "/ws-caro" endpoint, enabling the SockJS fallback options
        registry.addEndpoint("/ws-caro")
                .setAllowedOrigins("*") // For development - adjust in production
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable a simple memory-based message broker
        config.enableSimpleBroker("/topic"); // Prefix for messages to clients
        config.setApplicationDestinationPrefixes("/app"); // Prefix for messages to server
    }
}