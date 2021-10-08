package com.logicgate.sample.config;

import lombok.RequiredArgsConstructor;
import org.neo4j.driver.AuthToken;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Config;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.AbstractNeo4jConfig;
import org.springframework.data.neo4j.core.DatabaseSelection;
import org.springframework.data.neo4j.core.DatabaseSelectionProvider;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jOperations;
import org.springframework.data.neo4j.core.Neo4jTemplate;

@Configuration
@RequiredArgsConstructor
public class Neo4jConfig extends AbstractNeo4jConfig {

  private final Neo4jProperties neo4jProperties;

  @Bean
  public Neo4jOperations neo4jTemplate() {
    return new Neo4jTemplate(Neo4jClient.create(driver()));
  }

  @Bean
  @Override
  public Driver driver() {
    Config config = Config.builder().withoutEncryption().withTrustStrategy(getStrategy()).build();
    AuthToken authToken = AuthTokens.basic(neo4jProperties.getUsername(), neo4jProperties.getPassword());

    return GraphDatabase.driver(neo4jProperties.getUrl(), authToken, config);
  }

  @Bean
  @Override
  protected DatabaseSelectionProvider databaseSelectionProvider() {
    return () -> DatabaseSelection.byName(neo4jProperties.getDatabase());
  }

  private Config.TrustStrategy getStrategy() {
    return Config.TrustStrategy.trustAllCertificates().withoutHostnameVerification();
  }
}
