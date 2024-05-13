package rest2soap.api;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.StreamingFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import rest2soap.model.Contribuyente;
import rest2soap.model.ContribuyenteRequest;
import rest2soap.model.RecargoEquivalenciaResponse;
import rest2soap.service.NifSoapService;
import rest2soap.service.RecargoEquivalenciaSoapService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Stream;


@Controller
public class RestController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    private final NifSoapService nifSoapService;

    private final RecargoEquivalenciaSoapService recargoEquivalenciaSoapService;

    public RestController(NifSoapService nifSoapService,
                          RecargoEquivalenciaSoapService recargoEquivalenciaSoapService) {
        this.nifSoapService = nifSoapService;
        this.recargoEquivalenciaSoapService = recargoEquivalenciaSoapService;
    }

    @Post("/cifs")
    Flux<Contribuyente> cifs(@Body List<String> request)  {
        logger.info("Validate cifs {}", request);
        return Flux.fromStream(()-> nifSoapService.checkCifs(request) );
    }

    @Post("/cif")
    Mono<Contribuyente> cifs(String cif)  {
        logger.info("Validate cifs {}", cif);
        return Mono.fromCallable(()->
                nifSoapService.checkCifs(List.of(cif)).findFirst().orElse(new Contribuyente(
                        cif, "", false, "NO ENCONTRADO"
                )) );
    }

    @Post("/nifs")
    Flux<Contribuyente> nifs(@Body List<ContribuyenteRequest> contribuyentes)  {
        logger.info("Validate nifs {}", contribuyentes.size());
        return Flux.fromStream(()-> nifSoapService.checkNifs(contribuyentes));
    }

    @Post("/nif")
    Mono<Contribuyente> nif(@Body ContribuyenteRequest contribuyente)  {
        logger.info("Validate nif {}", contribuyente.nif());
        return Mono.fromCallable(()->
                nifSoapService.checkNifs(List.of(contribuyente)).findFirst().orElse(new Contribuyente(
                        contribuyente.nif(), contribuyente.nombre(), false, "NO ENCONTRADO"
                )));
    }

    @Get("/recargo/{nif}")
    Mono<RecargoEquivalenciaResponse> recargoEquivalencia(String nif)  {
        logger.info("Validate recargo equivalencia {}", nif);
        return Mono.fromCallable(()->
                recargoEquivalenciaSoapService.checkRecargoEquivalencia(nif));
    }

    @Post(value="/recargo/", consumes = {MediaType.MULTIPART_FORM_DATA}, produces = {MediaType.APPLICATION_JSON})
    Flux<String> filterRecargoEquivalencia(StreamingFileUpload file) throws IOException {
        logger.info("Filter recargo equivalencia {} bytes", file.getSize());
        var tmp = Files.createTempFile(file.getFilename(), "temp");
        return Flux.from(file.transferTo(tmp.toFile())).subscribeOn(Schedulers.boundedElastic())
                .flatMap(f->{
                    try {
                        Stream<String> list =Files.lines(tmp);
                        return Flux.fromStream(recargoEquivalenciaSoapService.filter(list));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
