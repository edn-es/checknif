package rest2soap.model;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record ContribuyenteRequest(String nif, String nombre) {
}
