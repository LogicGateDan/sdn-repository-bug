package com.logicgate.sample.dummy;

import com.logicgate.sample.domain.EvilPerson;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvilPersonRepository extends Neo4jRepository<EvilPerson, String> {
  // dummy
}
