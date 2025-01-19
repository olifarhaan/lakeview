package com.olifarhaan.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class RedissonConfig {

    @Bean
    @Profile("dev")
    public RedissonClient redissonClientDev(@Value("${spring.redis.host}") String REDIS_HOST,
            @Value("${spring.redis.port}") int REDIS_PORT) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(String.format("redis://%s:%d", REDIS_HOST, REDIS_PORT));
        return Redisson.create(config);
    }

    @Bean
    @Profile("!dev")
    public RedissonClient redissonClient(@Value("${spring.redis.connection-string}") String REDIS_CONNECTION_STRING) {
        Config config = new Config();
        config.useSingleServer().setAddress(REDIS_CONNECTION_STRING);
        return Redisson.create(config);
    }

}