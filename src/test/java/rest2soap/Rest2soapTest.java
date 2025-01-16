package rest2soap;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.multipart.MultipartBody;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rest2soap.model.ContribuyenteRequest;
import rest2soap.repository.Member;
import rest2soap.repository.MemberRepository;

import java.net.URI;
import java.nio.file.Files;
import java.util.List;

@MicronautTest
class Rest2soapTest {

    @Inject
    EmbeddedApplication<?> application;

    @Inject
    MemberRepository memberRepository;

    @BeforeEach
    void setup() {
        memberRepository.save(new Member(1L, "123456"));
    }

    @AfterEach
    void cleanup() {
        memberRepository.deleteAll();
    }

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

    @Test
    void testCifs(@Client("/") HttpClient httpClient) throws Exception{
        BlockingHttpClient client = httpClient.toBlocking();
        URI uri = UriBuilder.of("/").build();
        var str = client.retrieve(HttpRequest.POST(uri, List.of(
                new ContribuyenteRequest("B80988801",null))
        ).header("X-API-Key","123456"));
        System.out.println(str);
    }

    @Test
    void testCifsNifs(@Client("/") HttpClient httpClient) throws Exception{
        BlockingHttpClient client = httpClient.toBlocking();
        URI uri = UriBuilder.of("/").build();
        var str = client.retrieve(HttpRequest.POST(uri, List.of(
                new ContribuyenteRequest("B80988801",null),
                new ContribuyenteRequest("123456789Q","Juan"))
        ).header("X-API-Key","123456"));
        System.out.println(str);
    }


    @Test
    void testCif(@Client("/") HttpClient httpClient) throws Exception{
        BlockingHttpClient client = httpClient.toBlocking();
        URI uri = UriBuilder.of("/123456/B80988801").build();
        var str = client.retrieve(HttpRequest.GET(uri));
        System.out.println(str);
    }

    @Test
    void testNifs(@Client("/") HttpClient httpClient) throws Exception{
        BlockingHttpClient client = httpClient.toBlocking();
        URI uri = UriBuilder.of("/").build();
        var str = client.retrieve(HttpRequest.POST(uri, List.of(new ContribuyenteRequest(
                "123456789Q", "Juan"
        ))).header("X-API-Key","123456"));
        System.out.println(str);
    }

    @Test
    void testNif(@Client("/") HttpClient httpClient) throws Exception{
        BlockingHttpClient client = httpClient.toBlocking();
        URI uri = UriBuilder.of("/123456/123456789Q/Juan").build();
        var str = client.retrieve(HttpRequest.GET(uri));
        System.out.println(str);
    }

    @Test
    void testRecargo(@Client("/") HttpClient httpClient) throws Exception{
        BlockingHttpClient client = httpClient.toBlocking();
        URI uri = UriBuilder.of("/").path("/123456/recargo/50954791Q").build();
        var str = client.retrieve(HttpRequest.GET(uri));
        System.out.println(str);
    }

    @Test
    void testFilterRecargo(@Client("/") HttpClient httpClient) throws Exception{
        BlockingHttpClient client = httpClient.toBlocking();
        URI uri = UriBuilder.of("/").path("/recargo").build();
        var tmp = Files.createTempFile("checknif", "tmp");
        Files.writeString(tmp,"123456789Q");
        MultipartBody requestBody = MultipartBody.builder()
                .addPart("file", "file.tmp",tmp.toFile()).build();
        HttpRequest<?> req = HttpRequest.POST(uri,
                requestBody).header("X-API-Key","123456").contentType(MediaType.MULTIPART_FORM_DATA);
        var str = client.retrieve(req);
        System.out.println(str);
    }
}
