package org.xdi.oxd.licenser.server.service;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xdi.oxd.license.client.js.LdapLicenseId;
import org.xdi.oxd.license.client.js.LicenseMetadata;
import org.xdi.oxd.license.client.js.Product;

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

    public void validateHours(int hours) {
        if (hours <=0 || hours > 1000) {
            throw new WebApplicationException(ErrorService.response("'hours' must be in range [1..1000]"));
        }
    }

    public void validate(LicenseMetadata metadata) {
        if (Product.fromValue(metadata.getProduct()) == null) {
            throw new WebApplicationException(ErrorService.response("'product' attribute is not valid or empty. Supported product values are: " + Product.supportedProductsString()));
        }
        if (metadata.getExpirationDate() == null) {
            throw new WebApplicationException(ErrorService.response("'expiration_date' is not set"));
        }
    }
}
