package com.polarbookshop.catalogservice.domain;

import lombok.AccessLevel;
import lombok.With;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

@With(AccessLevel.PRIVATE)
public record BookReference(AggregateReference<Book, Long> referencedBook) {
}
