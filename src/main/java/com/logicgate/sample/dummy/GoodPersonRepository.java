package com.logicgate.sample.dummy;

import com.logicgate.sample.domain.GoodPerson;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodPersonRepository extends Neo4jRepository<GoodPerson, String> {
  // dummy
}
