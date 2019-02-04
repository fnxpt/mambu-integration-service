package com.backbase.dbs.capabilities.extended.mambu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Client config class
 */
@Configuration
public class ClientConfiguration {

    /**
     * Create and inject RestTemplate into context
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
