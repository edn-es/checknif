package rest2soap.service;

import es.gob.agenciatributaria.www2.static_files.common.internet.dep.aplicaciones.es.aeat.bugc.jdit.ws.comprecequivent_xsd.CompRecEquivEnt;
import es.gob.agenciatributaria.www2.static_files.common.internet.dep.aplicaciones.es.aeat.bugc.jdit.ws.comprecequivent_xsd.CompRecEquivEntE;
import es.gob.agenciatributaria.www2.static_files.common.internet.dep.aplicaciones.es.aeat.bugc.jdit.ws.comprecequivent_xsd.Contribuyente_type0;
import es.gob.agenciatributaria.www2.static_files.common.internet.dep.aplicaciones.es.aeat.bugc.jdit.ws.comprecequivu_wsdl.CompRecEquivUServiceCompRecEquivUPort3Stub;
import io.micronaut.context.annotation.Context;
import jakarta.inject.Singleton;
import org.apache.axis2.transport.http.HTTPConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rest2soap.config.SslFactory;
import rest2soap.model.RecargoEquivalenciaResponse;

@Singleton
@Context
public class RecargoEquivalenciaSoapService {

    private static final Logger logger = LoggerFactory.getLogger(RecargoEquivalenciaSoapService.class);
    private final CompRecEquivUServiceCompRecEquivUPort3Stub recEquivStub;

    public RecargoEquivalenciaSoapService(SslFactory sslFactory) throws Exception {
        this.recEquivStub = new CompRecEquivUServiceCompRecEquivUPort3Stub();
        this.recEquivStub._getServiceClient()
                .getOptions()
                .setProperty(HTTPConstants.CACHED_HTTP_CLIENT, sslFactory.sslEnabledHttpClient());
    }

    public RecargoEquivalenciaResponse checkRecargoEquivalencia(String nif) {
        var contribuyente = new Contribuyente_type0();
        contribuyente.setNif(nif);
        contribuyente.setNombre(" ");
        return check(contribuyente);
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
            logger.error("Error checking recargo", e);
            return new RecargoEquivalenciaResponse(contribuyente.getNif(), false, e.getMessage());
        }
    }

}
