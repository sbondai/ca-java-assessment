package com.ca.javaassessment.db;

import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(classes = { LiquibaseMigrationTest.LiquibaseTestConfig.class })

public class LiquibaseMigrationTest {

    @Autowired
    private SpringLiquibase liquibase;

    private DataSource dataSource;

    @BeforeEach
    public void setUp() {
        dataSource = DataSourceBuilder.create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
                .username("sa")
                .password("")
                .build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        dataSource.getConnection().createStatement().execute("DROP ALL OBJECTS");
        dataSource.getConnection().close();
    }

    @Test
    public void testLiquibaseMigrations() {
        assertDoesNotThrow(() -> {
            liquibase.afterPropertiesSet();
        });
    }

    @Configuration
    static class LiquibaseTestConfig {

        @Bean
        public DataSource dataSource() {
            return DataSourceBuilder.create()
                    .driverClassName("org.h2.Driver")
                    .url("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
                    .username("sa")
                    .password("")
                    .build();
        }

        @Bean
        public SpringLiquibase liquibase(DataSource dataSource) {
            SpringLiquibase liquibase = new SpringLiquibase();
            liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.xml");
            liquibase.setDataSource(dataSource);
            return liquibase;
        }
    }
}
