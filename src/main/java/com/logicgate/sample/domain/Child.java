package com.logicgate.sample.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Getter
@Setter
@Node("Child")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Child extends Parent {

  private static final long serialVersionUID = 1016247578229462447L;

  @Property
  private final String name;

  public Child(String id, String name) {
    super(id);
    this.name = name;
  }
}
