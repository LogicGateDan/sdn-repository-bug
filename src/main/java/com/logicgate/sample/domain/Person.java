package com.logicgate.sample.domain;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("Person")
public abstract class Person {

  @Id
  protected final String id;
}
