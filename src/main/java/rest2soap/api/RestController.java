package rest2soap.api;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.multipart.StreamingFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import rest2soap.model.Contribuyente;
import rest2soap.model.ContribuyenteRequest;
import rest2soap.model.RecargoEquivalenciaResponse;
import rest2soap.service.MemberService;
import rest2soap.service.NifSoapService;
import rest2soap.service.RecargoEquivalenciaSoapService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


@Controller
public class RestController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    private final NifSoapService nifSoapService;

    private final RecargoEquivalenciaSoapService recargoEquivalenciaSoapService;

    private final MemberService memberService;

    public RestController(NifSoapService nifSoapService,
                          RecargoEquivalenciaSoapService recargoEquivalenciaSoapService,
                          MemberService memberService) {
        this.nifSoapService = nifSoapService;
        this.recargoEquivalenciaSoapService = recargoEquivalenciaSoapService;
        this.memberService = memberService;
    }

    @Post("/")
    Flux<Contribuyente> nifs(@Body List<ContribuyenteRequest> contribuyentes, @Header("X-API-Key") String apiKey)  {
        memberService.validate(apiKey);
        logger.info("Validate nifs {}", contribuyentes.size());
        return Flux.fromStream(()-> nifSoapService.checkNifs(contribuyentes));
    }

    @Get("/{apiKey}/{nif}")
    Mono<Contribuyente> cif(String nif, String apiKey)  {
        memberService.validate(apiKey);
        logger.info("Validate nif {}", nif);
        return Mono.fromCallable(()->
                nifSoapService.checkCifs(List.of(nif)).findFirst().orElse(new Contribuyente(
                        nif, "", false, "NO ENCONTRADO"
                )));
    }

    @Get("/{apiKey}/{nif}/{nombre}")
    Mono<Contribuyente> nif(String nif, String nombre, String apiKey)  {
        memberService.validate(apiKey);
        logger.info("Validate nif {}", nif);
        return Mono.fromCallable(()->
                nifSoapService.checkNifs(List.of(new ContribuyenteRequest(nif, nombre))).findFirst().orElse(new Contribuyente(
                        nif, nombre, false, "NO ENCONTRADO"
                )));
    }

    @Get("/{apiKey}/recargo/{nif}")
    Mono<RecargoEquivalenciaResponse> recargoEquivalencia(String nif, String apiKey) {
        memberService.validate(apiKey);
        logger.info("Validate recargo equivalencia {}", nif);
        return Mono.fromCallable(()->
                recargoEquivalenciaSoapService.checkRecargoEquivalencia(nif));
    }

    @Post(value="/recargo/", consumes = {MediaType.MULTIPART_FORM_DATA}, produces = {MediaType.APPLICATION_JSON})
    Flux<String> filterRecargoEquivalencia(StreamingFileUpload file, @Header("X-API-Key") String apiKey) throws IOException {
        memberService.validate(apiKey);
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
