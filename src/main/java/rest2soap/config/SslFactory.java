package rest2soap.config;

import jakarta.inject.Singleton;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import rest2soap.config.CheckNifConfig;

import javax.net.ssl.SSLContext;
import java.io.File;

@Singleton
public class SslFactory {

    private final CheckNifConfig checkNifConfig;

    public SslFactory(CheckNifConfig checkNifConfig) {
        this.checkNifConfig = checkNifConfig;
    }

    public HttpClient sslEnabledHttpClient() throws Exception{
        final SSLContextBuilder builder = SSLContexts.custom()
                .loadKeyMaterial(
                        new File(checkNifConfig.getCertificado()),
                        checkNifConfig.getPassword().toCharArray(),
                        checkNifConfig.getPassword().toCharArray(),null);
        final SSLContext sslContext = builder.build();
        return HttpClientBuilder.create().setSSLContext(sslContext).build();
    }
}
