package com.logicgate.sample.repository;

import com.logicgate.sample.domain.Person;
import java.util.Collection;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends Neo4jRepository<Person, String> {

  @Query("MATCH (person:Person) WHERE person.id IN $ids "
      + "RETURN person;")
  Collection<Person> findPeopleByIdsIn(Collection<String> ids);
}
