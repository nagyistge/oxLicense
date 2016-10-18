package org.xdi.oxd.licenser.server.ws;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xdi.oxd.licenser.server.service.ErrorService;
import org.xdi.oxd.licenser.server.service.StatisticService;
import org.xdi.oxd.licenser.server.service.ValidationService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 06/09/2016
 */

@Path("/rest")
public class StatisticWS {

    private static final Logger LOG = LoggerFactory.getLogger(StatisticWS.class);

    @Inject
    ValidationService validationService;
    @Inject
    ErrorService errorService;
    @Inject
    StatisticService statisticService;

    @GET
    @Path("/statistic")
    @Produces({MediaType.APPLICATION_JSON})
    public Response generateGet(@QueryParam("licenseId") String licenseId) {
        LOG.debug("Statistic request, licenseId: " + licenseId);
        validationService.getLicenseId(licenseId);

        return Response.ok().entity(statisticService.monthlyStatistic(licenseId)).build();
    }
}
