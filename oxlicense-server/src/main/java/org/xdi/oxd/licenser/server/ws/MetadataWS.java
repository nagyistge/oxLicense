package org.xdi.oxd.licenser.server.ws;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xdi.oxd.license.client.Jackson;
import org.xdi.oxd.license.client.js.LdapLicenseId;
import org.xdi.oxd.license.client.js.LicenseMetadata;
import org.xdi.oxd.licenser.server.service.ErrorService;
import org.xdi.oxd.licenser.server.service.LicenseIdService;
import org.xdi.oxd.licenser.server.service.ValidationService;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Returns metadata for given license id.
 * <p/>
 * Created by yuriy on 8/15/2015.
 */
@Path("/rest")
public class MetadataWS {

    private static final Logger LOG = LoggerFactory.getLogger(MetadataWS.class);

    @Inject
    ValidationService validationService;
    @Inject
    ErrorService errorService;
    @Inject
    LicenseIdService licenseIdService;

    @GET
    @Path("/metadata")
    @Produces({MediaType.APPLICATION_JSON})
    public Response generateGet(@QueryParam("licenseId") String licenseId) {
        LOG.debug("Metadata request, licenseId: " + licenseId);
        String json = Jackson.asJsonSilently(metadata(licenseId));
        return Response.ok().entity(json).build();
    }

    @PUT
    @Path("/metadata")
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(LicenseMetadata metadata) {
        try {
            LOG.debug("Updating licenseId: " + metadata.getLicenseId() + ", metadata: " + metadata);
            validationService.validate(metadata);

            LdapLicenseId licenseId = validationService.getLicenseId(metadata.getLicenseId());
            licenseId.setMetadataAsObject(metadata);
            licenseId.setMetadata(Jackson.asJsonSilently(metadata));
            licenseIdService.merge(licenseId);

            LOG.debug("LicenseId: " + metadata.getLicenseId() + " updated.");
            return Response.ok().build();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new WebApplicationException(ErrorService.response(e.getMessage()));
        }
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
