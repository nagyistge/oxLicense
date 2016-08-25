package org.xdi.oxd.license.client;

import org.xdi.oxd.license.client.data.LicenseResponse;
import org.xdi.oxd.license.client.js.LicenseIdItem;
import org.xdi.oxd.license.client.js.LicenseMetadata;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 07/09/2014
 */

@Path("/rest")
public interface GenerateWS {

    @GET
    @Path("/generate")
    @Produces(MediaType.APPLICATION_JSON)
    List<LicenseResponse> generateGet(@QueryParam("licenseId") String licenseId);

    @POST
    @Path("/generate")
    @Produces(MediaType.APPLICATION_JSON)
    List<LicenseResponse> generatePost(@FormParam("licenseId") String licenseId);

    @POST
    @Path("/generateLicenseId/{licenseCount}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    List<LicenseIdItem> generateLicenseId(@PathParam("licenseCount") int licenseCount, @HeaderParam("Authorization") String authorization, LicenseMetadata licenseMetadata);

}
