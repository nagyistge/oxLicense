package org.xdi.oxd.licenser.server.ws;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xdi.oxd.license.client.Jackson;
import org.xdi.oxd.license.client.js.LdapLicenseId;
import org.xdi.oxd.license.client.js.LicenseMetadata;
import org.xdi.oxd.licenser.server.service.ValidationService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Returns metadata for given license id.
 *
 * Created by yuriy on 8/15/2015.
 */
@Path("/rest")
public class MetadataWS {

    private static final Logger LOG = LoggerFactory.getLogger(MetadataWS.class);

    @Inject
    ValidationService validationService;
    @Inject
    ErrorService errorService;

    @GET
    @Path("/metadata")
    @Produces({MediaType.APPLICATION_JSON})
    public Response generateGet(@QueryParam("licenseId") String licenseId, @Context HttpServletRequest httpRequest) {

        LOG.debug("Metadata request, licenseId: " + licenseId);
        String json = Jackson.asJsonSilently(metadata(licenseId));
        return Response.ok().entity(json).build();
    }

    public LicenseMetadata metadata(String licenseIdStr) {
        try {
            LdapLicenseId licenseId = validationService.getLicenseId(licenseIdStr);
            return Jackson.createJsonMapper().readValue(licenseId.getMetadata(), LicenseMetadata.class);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new WebApplicationException(ErrorService.response(e.getMessage()));
        }
    }

}
