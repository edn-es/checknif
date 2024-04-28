package rest2soap.model;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record Contribuyente(String nif, String nombre, boolean result, String resultado) {
}
