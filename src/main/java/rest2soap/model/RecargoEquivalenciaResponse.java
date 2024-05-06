package rest2soap.model;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record RecargoEquivalenciaResponse(String nif, boolean enRecargo, String descripcion) {
}
