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

}
