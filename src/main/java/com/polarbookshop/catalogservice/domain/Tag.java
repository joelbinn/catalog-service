package com.polarbookshop.catalogservice.domain;

import lombok.With;

@With
public record Tag(String name, String value) {
}
