package com.logicgate.sample.repository;

import com.logicgate.sample.domain.Child;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildRepository extends Neo4jRepository<Child, String> {

  @Query("MATCH (child:Child {id: $id}) RETURN child;")
  Child findChildById(String id);
}
