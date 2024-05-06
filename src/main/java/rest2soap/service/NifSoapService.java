package rest2soap.service;

import es.gob.agenciatributaria.www2.static_files.common.internet.dep.aplicaciones.es.aeat.burt.jdit.ws.vnifv2_wsdl.VNifV2ServiceStub;
import es.gob.agenciatributaria.www2.static_files.common.internet.dep.aplicaciones.es.aeat.burt.jdit.ws.vnifv2ent_xsd.Contribuyente_type0;
import es.gob.agenciatributaria.www2.static_files.common.internet.dep.aplicaciones.es.aeat.burt.jdit.ws.vnifv2ent_xsd.VNifV2Ent;
import es.gob.agenciatributaria.www2.static_files.common.internet.dep.aplicaciones.es.aeat.burt.jdit.ws.vnifv2ent_xsd.VNifV2EntE;
import io.micronaut.context.annotation.Context;
import jakarta.inject.Singleton;
import org.apache.axis2.transport.http.HTTPConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rest2soap.model.Contribuyente;
import rest2soap.config.SslFactory;
import rest2soap.model.ContribuyenteRequest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Singleton
@Context
public class NifSoapService {

    private static final Logger logger = LoggerFactory.getLogger(NifSoapService.class);
    private static final String IDENTIFICADO = "IDENTIFICADO";

    private final VNifV2ServiceStub vNifV2ServiceStub;

    public NifSoapService(SslFactory sslFactory) throws Exception {
        this.vNifV2ServiceStub = new VNifV2ServiceStub();
        this.vNifV2ServiceStub._getServiceClient()
                .getOptions()
                .setProperty(HTTPConstants.CACHED_HTTP_CLIENT, sslFactory.sslEnabledHttpClient());
    }

    public Stream<Contribuyente> checkCifs(List<String> cifs) {
        var contribuyentes = cifs.stream().map(c->{
            var ret = new Contribuyente_type0();
            ret.setNif(c);
            ret.setNombre("");
            return ret;
        }).toList();
        return check(contribuyentes);
    }

    public Stream<Contribuyente> checkNifs(List<ContribuyenteRequest> request) {
        var contribuyentes = request.stream().map(c->{
            var ret = new Contribuyente_type0();
            ret.setNif(c.nif());
            ret.setNombre(c.nombre());
            return ret;
        }).toList();
        return check(contribuyentes);
    }

    protected Stream<Contribuyente> check(List<Contribuyente_type0> contribuyentes) {

        var ent = new VNifV2Ent();
        ent.setContribuyente( contribuyentes.toArray(new Contribuyente_type0[0]) );

        var query = new VNifV2EntE();
        query.setVNifV2Ent(ent);

        try {
            var sal = vNifV2ServiceStub.vNifV2(query).getVNifV2Sal();
            return Arrays.stream(sal.getContribuyente()).map(s->{
                boolean ok =IDENTIFICADO.equalsIgnoreCase(s.getResultado());
                String result = s.getResultado();
                return new Contribuyente(s.getNif(), s.getNombre().trim(), ok, result);
            });
        }catch(Exception e){
            logger.error("Error checking cifs", e);
            return Stream.of();
        }
    }

}
