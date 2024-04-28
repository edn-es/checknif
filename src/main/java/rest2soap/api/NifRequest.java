package rest2soap.api;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record NifRequest(String nif, String nombre) {
}
