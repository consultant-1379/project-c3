package com.dao.gerritrepo;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfiguration {
    @Value("${mongoDbName}")
    private String mongoDbName;

    @Primary
    @Bean
    public GerritRepoService gerritRepoService() {
        return new GerritRepoServiceImpl();
    }

    @Bean
    public GerritRepoDAO gerritRepoDAO() {
        return new GerritRepoDAO();
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(new MongoClient(), mongoDbName);
    }
}