package rest2soap.config;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("checknif")
public class CheckNifConfig {

    private String certificado;
    private String password;

    public String getCertificado() {
        return certificado;
    }

    public void setCertificado(String certificado) {
        this.certificado = certificado;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
