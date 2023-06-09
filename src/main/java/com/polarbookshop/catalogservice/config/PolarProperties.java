package com.polarbookshop.catalogservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "polar")
@Getter
@Setter
public class PolarProperties {
  /** This is the greeting that will be used in the application. */
  private String greeting;
}
