package rest2soap.api;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rest2soap.model.Contribuyente;
import rest2soap.model.ContribuyenteRequest;
import rest2soap.model.RecargoEquivalenciaResponse;
import rest2soap.service.NifSoapService;
import rest2soap.service.RecargoEquivalenciaSoapService;

import java.util.List;


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
}
