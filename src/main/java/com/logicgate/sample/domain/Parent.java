package com.logicgate.sample.domain;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;

@Data
public abstract class Parent {

  @Id
  protected final String id;
}
