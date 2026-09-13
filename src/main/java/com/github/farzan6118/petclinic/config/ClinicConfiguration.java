package com.github.farzan6118.petclinic.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ClinicProperties.class)
public class ClinicConfiguration {
}