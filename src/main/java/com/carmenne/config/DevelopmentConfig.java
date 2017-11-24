package com.carmenne.config;

import com.carmenne.backend.service.EmailService;
import com.carmenne.backend.service.MockEmailService;
import org.h2.server.web.WebServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("dev")
@PropertySource("file:///${user.home}/projects/files_s/application-dev.properties")
public class DevelopmentConfig
{
    @Bean
    public EmailService emailService() {

        return new MockEmailService();
    }

    @Bean
    public ServletRegistrationBean h2ConsoleServletRegistration() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new WebServlet());
        bean.addUrlMappings("/console/*");
        return bean;
    }

}
