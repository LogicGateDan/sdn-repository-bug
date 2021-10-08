package com.logicgate.sample.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.logicgate.sample.Application;
import com.logicgate.sample.config.Neo4jConfig;
import com.logicgate.sample.config.Neo4jProperties;
import com.logicgate.sample.domain.Child;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DirtiesContext
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    initializers = ConfigDataApplicationContextInitializer.class,
    classes = {
        Application.class,
        Neo4jConfig.class,
        Neo4jProperties.class,
        ChildRepository.class
    }
)
@EnableNeo4jRepositories(basePackageClasses = ChildRepository.class)
public class ChildRepositoryTest {

  @Autowired
  private ChildRepository childRepository;

  @Autowired
  private Neo4jClient neo4jClient;

  @BeforeEach
  public void setup() {
    neo4jClient.query("CREATE (child:Child {id: 'child-1', name: 'child entity'});").run();
  }

  @AfterEach
  public void teardown() {
    neo4jClient.query("MATCH (n) DETACH DELETE n;").run();
  }

  @Test
  public void findById() {
    Child expected = new Child("child-1", "child entity");
    Child child = childRepository.findChildById("child-1");
    assertThat(child).isEqualTo(expected);
  }
}
