package com.logicgate.sample.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@Accessors(chain = true)
@ConfigurationProperties(prefix = "neo4j")
public class Neo4jProperties {

  private String certPath;

  private String database;

  private String password;

  private Integer poolSize;

  private String url;

  private String username;
}
