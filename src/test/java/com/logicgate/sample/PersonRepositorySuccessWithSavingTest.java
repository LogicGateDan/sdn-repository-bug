package com.logicgate.sample;

import static org.assertj.core.api.Assertions.assertThat;

import com.logicgate.sample.config.Neo4jConfig;
import com.logicgate.sample.config.Neo4jProperties;
import com.logicgate.sample.domain.EvilPerson;
import com.logicgate.sample.domain.GoodPerson;
import com.logicgate.sample.domain.Person;
import com.logicgate.sample.repository.PersonRepository;
import java.util.Collection;
import java.util.List;
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
        PersonRepository.class
    }
)
@EnableNeo4jRepositories(basePackageClasses = PersonRepository.class)
public class PersonRepositorySuccessWithSavingTest {

  @Autowired
  private PersonRepository personRepository;

  @Autowired
  private Neo4jClient neo4jClient;

  private GoodPerson goodPerson;
  private EvilPerson evilPerson;

  @BeforeEach
  public void setup() {
    goodPerson = personRepository.save(new GoodPerson("p-1", "helpful stuff"));
    evilPerson = personRepository.save(new EvilPerson("p-2", "mean stuff"));
  }

  @AfterEach
  public void teardown() {
    neo4jClient.query("MATCH (n) DETACH DELETE n;").run();
  }

  /**
   * This should work.
   */
  @Test
  public void findPeopleByIdsIn() {
    Collection<Person> people = personRepository.findPeopleByIdsIn(List.of(goodPerson.getId(), evilPerson.getId()));
    assertThat(people).containsExactlyInAnyOrder(goodPerson, evilPerson);
  }
}
