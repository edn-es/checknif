package rest2soap.model;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record ContribuyenteRequest(String nif, @Nullable String nombre) {
}
