package org.xdi.oxd.licenser.server.service;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xdi.oxd.license.client.js.LdapLicenseId;

import javax.ws.rs.WebApplicationException;

/**
 * Created by yuriy on 8/15/2015.
 */
public class ValidationService {

    private static final Logger LOG = LoggerFactory.getLogger(ValidationService.class);

    @Inject
    LicenseIdService licenseIdService;

    public LdapLicenseId getLicenseId(String licenseId) {
        try {
            if (!Strings.isNullOrEmpty(licenseId)) {
                final LdapLicenseId byId = licenseIdService.getById(licenseId);
                if (byId != null) {
                    return byId;
                }
            }
            LOG.error("License ID is blank" + licenseId);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        throw new WebApplicationException(ErrorService.response("Failed to find License ID with id: " + licenseId));
    }
}
