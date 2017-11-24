package com.carmenne.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages
        = "com.carmenne.backend.persistence.repositories")
@EntityScan(basePackages = "com.carmenne.backend.persistence.domain.backend")
@EnableTransactionManagement
@PropertySource("file:///${user.home}/projects/files_s/application-common.properties")
public class ApplicationConfig {

}
