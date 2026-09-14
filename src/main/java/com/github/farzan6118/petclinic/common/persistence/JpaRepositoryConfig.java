package com.github.farzan6118.petclinic.common.persistence;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.github.farzan6118.petclinic.repository")
public class JpaRepositoryConfig {
}