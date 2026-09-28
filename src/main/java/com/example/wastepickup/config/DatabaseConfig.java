package com.example.wastepickup.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;

@Configuration
public class DatabaseConfig {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${spring.datasource.url}")
    private String mysqlUrl;

    @Value("${spring.datasource.username}")
    private String mysqlUser;

    @Value("${spring.datasource.password}")
    private String mysqlPassword;

    @Bean
    @Primary
    public DataSource dataSource() {
        // If password is placeholder or empty or MySQL is unavailable, fallback gracefully to H2
        if ("YOUR_MYSQL_PASSWORD_HERE".equalsIgnoreCase(mysqlPassword) || mysqlPassword == null || mysqlPassword.trim().isEmpty()) {
            log.warn("==========================================================================================");
            log.warn(" [NOTICE] MySQL password is set to default placeholder: 'YOUR_MYSQL_PASSWORD_HERE'");
            log.warn(" Switching to in-memory H2 database (MySQL Mode) so the app runs immediately!");
            log.warn(" To use your MySQL server, update 'spring.datasource.password' in application.properties.");
            log.warn("==========================================================================================");
            return createH2DataSource();
        }

        try {
            // Test connection to MySQL
            try (Connection conn = DriverManager.getConnection(mysqlUrl, mysqlUser, mysqlPassword)) {
                log.info("Successfully connected to MySQL database at: {}", mysqlUrl);
            }
            return DataSourceBuilder.create()
                    .driverClassName("com.mysql.cj.jdbc.Driver")
                    .url(mysqlUrl)
                    .username(mysqlUser)
                    .password(mysqlPassword)
                    .build();
        } catch (Exception e) {
            log.warn("==========================================================================================");
            log.warn(" [WARNING] Failed to connect to MySQL: {}", e.getMessage());
            log.warn(" Falling back to in-memory H2 database (MySQL Mode) so you can test and use the application!");
            log.warn("==========================================================================================");
            return createH2DataSource();
        }
    }

    private DataSource createH2DataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:mem:wastepickup_db;DB_CLOSE_DELAY=-1;MODE=MySQL;NON_KEYWORDS=USER,VALUE")
                .username("sa")
                .password("")
                .build();
    }
}
