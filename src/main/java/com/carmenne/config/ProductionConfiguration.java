package com.carmenne.config;

import com.carmenne.backend.service.EmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("prod")
@PropertySource("file:///${user.home}/projects/devops/application-prod.properties")
public class ProductionConfiguration {

    @Bean
    public EmailService emailService() {
        return new SmtpEmailService();
    }
}
