package rest2soap.repository;

import io.micronaut.data.annotation.EntityRepresentation;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import jakarta.annotation.Nonnull;

@MappedEntity("personal_access_tokens")
public record Member(
        @Id Long id,
        @Nonnull
        @MappedProperty("token")
        String apiKey
) {
}
