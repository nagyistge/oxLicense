package org.xdi.oxd.licenser.server.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xdi.oxd.license.client.Jackson;
import org.xdi.oxd.license.client.data.ErrorResponse;
import org.xdi.oxd.license.client.data.ErrorType;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 09/11/2014
 */

public class ErrorService {

    private static final Logger LOG = LoggerFactory.getLogger(ErrorService.class);

    public void throwError(Response.StatusType status, String entity) throws WebApplicationException {
        LOG.trace("status: " + status + ", entity:" + entity);
        throw new WebApplicationException(Response.status(status).entity(entity).build());
    }

    public void throwError(Response.StatusType status, ErrorType errorType) throws WebApplicationException {
        throwError(status, Jackson.asJsonSilently(ErrorResponse.fromErrorType(errorType)));
    }

    public void throwWebApplicationException(Throwable e) throws WebApplicationException {
        LOG.error(e.getMessage());
        throw new WebApplicationException(response(e.getMessage()));
    }

    public void throwWebApplicationException(String responseEntity) throws WebApplicationException {
        LOG.error(responseEntity);
        throw new WebApplicationException(response(responseEntity));
    }

    public static Response response(String responseEntity) {
        return Response.status(Response.Status.BAD_REQUEST).entity(responseEntity).build();
    }

}
