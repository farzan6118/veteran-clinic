package com.github.farzan6118.petclinic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(
        basePackages = "com.github.farzan6118.petclinic.repository.redis"
)
public class RedisRepositoryConfig {
}