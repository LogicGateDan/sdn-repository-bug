package com.logicgate.sample.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Getter
@Setter
@Node("GoodPerson")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GoodPerson extends Person {

  @Property
  private final String favoriteCharity;

  public GoodPerson(String id, String favoriteCharity) {
    super(id);
    this.favoriteCharity = favoriteCharity;
  }
}
