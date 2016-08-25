package org.xdi.oxd.license.client;

import org.xdi.oxd.license.client.js.LicenseMetadata;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 25/08/2016
 */

@Path("/rest")
public interface MetadataWS {

    @GET
    @Path("/metadata")
    @Produces({MediaType.APPLICATION_JSON})
    public LicenseMetadata get(@QueryParam("licenseId") String licenseId);

    @PUT
    @Path("/metadata")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(@HeaderParam("Authorization") String authorization, LicenseMetadata metadata);
}
