package rest2soap;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import jakarta.inject.Inject;
import rest2soap.api.NifRequest;
import rest2soap.model.Contribuyente;

import java.net.URI;
import java.util.List;

@MicronautTest
class Rest2soapTest {

    @Inject
    EmbeddedApplication<?> application;

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

    @Test
    void testCifs(@Client("/") HttpClient httpClient) throws Exception{
        BlockingHttpClient client = httpClient.toBlocking();
        URI uri = UriBuilder.of("/").path("/cifs").build();
        var str = client.retrieve(HttpRequest.POST(uri, List.of("B1234456")));
        System.out.println(str);
    }

}
