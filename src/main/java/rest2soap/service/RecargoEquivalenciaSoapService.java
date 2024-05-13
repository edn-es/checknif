package rest2soap.service;

import es.gob.agenciatributaria.www2.static_files.common.internet.dep.aplicaciones.es.aeat.bugc.jdit.ws.comprecequivent_xsd.CompRecEquivEnt;
import es.gob.agenciatributaria.www2.static_files.common.internet.dep.aplicaciones.es.aeat.bugc.jdit.ws.comprecequivent_xsd.CompRecEquivEntE;
import es.gob.agenciatributaria.www2.static_files.common.internet.dep.aplicaciones.es.aeat.bugc.jdit.ws.comprecequivent_xsd.Contribuyente_type0;
import es.gob.agenciatributaria.www2.static_files.common.internet.dep.aplicaciones.es.aeat.bugc.jdit.ws.comprecequivu_wsdl.CompRecEquivUServiceCompRecEquivUPort3Stub;
import io.micronaut.context.annotation.Context;
import jakarta.inject.Singleton;
import org.apache.axis2.AxisFault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rest2soap.model.RecargoEquivalenciaResponse;

import java.util.stream.Stream;

@Singleton
@Context
public class RecargoEquivalenciaSoapService {

    private static final Logger logger = LoggerFactory.getLogger(RecargoEquivalenciaSoapService.class);
    private final CompRecEquivUServiceCompRecEquivUPort3Stub recEquivStub;

    public RecargoEquivalenciaSoapService() throws AxisFault {
        this.recEquivStub = new CompRecEquivUServiceCompRecEquivUPort3Stub();
    }

    public RecargoEquivalenciaResponse checkRecargoEquivalencia(String nif) {
        var contribuyente = new Contribuyente_type0();
        contribuyente.setNif(nif);
        contribuyente.setNombre(" ");
        return check(contribuyente);
    }

    public Stream<String> filter(Stream<String>nifs){
        return nifs.parallel().filter(nif->{
            var contribuyente = new Contribuyente_type0();
            contribuyente.setNif(nif);
            contribuyente.setNombre(" ");
            RecargoEquivalenciaResponse response = check(contribuyente);
            return response.enRecargo();
        }).distinct();
    }

    protected RecargoEquivalenciaResponse check(Contribuyente_type0 contribuyente) {

        var ent = new CompRecEquivEnt();
        ent.setContribuyente(contribuyente);

        var query = new CompRecEquivEntE();
        query.setCompRecEquivEnt(ent);

        try {
            var sal = recEquivStub.compRecEquivU(query).getCompRecEquivSal();
            boolean ok = sal.getContribuyente().getResultado().startsWith("NIF sometido al");
            String result = sal.getContribuyente().getResultado();
            return new RecargoEquivalenciaResponse(contribuyente.getNif(), ok, result);

        }catch(Exception e){
            logger.info("Error checking recargo {}", e.getMessage());
            return new RecargoEquivalenciaResponse(contribuyente.getNif(), false, "Error");
        }
    }

}
