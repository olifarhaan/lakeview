package com.olifarhaan.config;

import java.io.IOException;
import java.net.ServerSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import redis.embedded.RedisServer;

@Configuration
public class EmbeddedRedisConfig {

    private RedisServer redisServer;

    @Value("${spring.redis.port}")
    private int redisPort;

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedRedisConfig.class);

    @PostConstruct
    public void startRedis() {
        try {
            if (isPortInUse(redisPort)) {
                logger.error("Port " + redisPort + " is already in use. We are considering it is already running.");
                return;
            }

            redisServer = RedisServer.builder()
                    .port(redisPort)
                    .setting("maxheap 128M")
                    .build();
            redisServer.start();
            logger.info("Redis server started on port " + redisPort);
        } catch (Exception e) {
            throw new RuntimeException("Could not start Redis server", e);
        }
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
            logger.error("Redis server stopped on port " + redisPort);
        }
    }

    private boolean isPortInUse(int port) {
        try (ServerSocket socket = new ServerSocket(port)) {
            return false;
        } catch (IOException e) {
            return true;
        }
    }
}
