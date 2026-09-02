package com.github.farzan6118.petclinic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.github.farzan6118.petclinic.repository")
public class JpaRepositoryConfig {
}