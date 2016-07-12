package org.xdi.oxd.license.admin.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xdi.oxauth.model.jwt.Jwt;
import org.xdi.oxauth.model.jwt.JwtClaimName;
import org.xdi.oxd.license.admin.shared.IdTokenValidationResult;

import java.util.Date;
import java.util.List;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 12/07/2016
 */

public class IdTokenValidator {

    private static final Logger LOG = LoggerFactory.getLogger(IdTokenValidator.class);

    private final List<String> allowedUserEmails;

    public IdTokenValidator(List<String> allowedUserEmails) {
        this.allowedUserEmails = allowedUserEmails;
    }

    public IdTokenValidationResult hasAccess(String idToken) {
        try {
            final Jwt jwt = Jwt.parse(idToken);

            final Date expiresAt = jwt.getClaims().getClaimAsDate(JwtClaimName.EXPIRATION_TIME);
            final Date now = new Date();
            if (now.after(expiresAt)) {
                LOG.trace("ID Token is expired. (It is after " + now + ").");
                return IdTokenValidationResult.EXPIRED;
            }

            String email = jwt.getClaims().getClaimAsString("email");
            LOG.debug("email from id_token: " + email);
            if (allowedUserEmails.contains(email)) {
                return IdTokenValidationResult.ACCESS_GRANTED;

            }
        } catch (Exception e) {
            LOG.error("Failed to parse id_token: " + idToken, e);
        }
        return IdTokenValidationResult.CANT_PARSE;
    }
}
