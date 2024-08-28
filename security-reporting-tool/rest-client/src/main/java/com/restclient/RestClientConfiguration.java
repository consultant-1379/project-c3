package com.restclient;

import com.dao.gerritrepo.GerritRepoDAO;
import com.dao.gerritrepo.GerritRepoService;
import com.dao.gerritrepo.GerritRepoServiceImpl;
import com.mongodb.MongoClient;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfiguration {
    @Bean
    public RestClient restClient() {
        return new RestClientImpl();
    }
}
