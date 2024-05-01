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
import rest2soap.service.SoapService;

import java.util.List;


@Controller
public class RestController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    private final SoapService soapService;

    public RestController(SoapService soapService) {
        this.soapService = soapService;
    }

    @Post("/cifs")
    Flux<Contribuyente> cifs(@Body List<String> request)  {
        logger.info("Validate cifs {}", request);
        return Flux.fromStream(()-> soapService.checkCifs(request) );
    }

    @Post("/cif")
    Mono<Contribuyente> cifs(String cif)  {
        logger.info("Validate cifs {}", cif);
        return Mono.fromCallable(()->
                soapService.checkCifs(List.of(cif)).findFirst().orElse(new Contribuyente(
                        cif, "", false, "NO ENCONTRADO"
                )) );
    }

    @Post("/nifs")
    Flux<Contribuyente> nifs(@Body List<ContribuyenteRequest> contribuyentes)  {
        logger.info("Validate nifs {}", contribuyentes.size());
        return Flux.fromStream(()->soapService.checkNifs(contribuyentes));
    }

    @Post("/nif")
    Mono<Contribuyente> nif(@Body ContribuyenteRequest contribuyente)  {
        logger.info("Validate nif {}", contribuyente.nif());
        return Mono.fromCallable(()->
                soapService.checkNifs(List.of(contribuyente)).findFirst().orElse(new Contribuyente(
                        contribuyente.nif(), contribuyente.nombre(), false, "NO ENCONTRADO"
                )));
    }
}
