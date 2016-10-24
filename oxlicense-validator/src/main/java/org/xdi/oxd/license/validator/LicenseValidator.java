package org.xdi.oxd.license.validator;

import net.nicholaswilliams.java.licensing.SignedLicense;
import net.nicholaswilliams.java.licensing.exception.InvalidLicenseException;
import org.xdi.oxd.license.client.Jackson;
import org.xdi.oxd.license.client.js.LicenseMetadata;
import org.xdi.oxd.license.client.js.Product;
import org.xdi.oxd.license.client.lib.ALicense;
import org.xdi.oxd.license.client.lib.ALicenseManager;
import org.xdi.oxd.license.client.lib.LicenseSerializationUtilities;

import java.util.Date;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 27/05/2015
 */

public class LicenseValidator {


    /**
     *
     * @param publicKey public key
     * @param publicPassword public password
     * @param licensePassword  license password
     * @param license license
     * @param expectedProduct expected product (oxd, de)
     * @param currentDate current date (usually not system date because client can change date on local machine. It good idea to fetch current date from internet.)
     * @return output of license
     * @throws InvalidLicenseException
     */
    public static LicenseContent validate(String publicKey, String publicPassword, String licensePassword, String license,
                                  Product expectedProduct,
                                  Date currentDate) throws InvalidLicenseException {
        LicenseContent output = new LicenseContent();
        try {
            final SignedLicense signedLicense = LicenseSerializationUtilities.deserialize(license);

            ALicenseManager manager = new ALicenseManager(publicKey, publicPassword, signedLicense, licensePassword);

            ALicense decryptedLicense = manager.decryptAndVerifyLicense(signedLicense);// DECRYPT signed license
            manager.validateLicense(decryptedLicense);

            final LicenseMetadata metadata = Jackson.createJsonMapper().readValue(decryptedLicense.getSubject(), LicenseMetadata.class);
            validateMetadata(metadata, expectedProduct, currentDate);

            output.setValid(true);
            output.setMetadata(metadata);
            return output;
        } catch (Exception e) {
            throw new InvalidLicenseException(e);
        }
    }

    public static void validateMetadata(LicenseMetadata metadata, Product expectedProduct, Date currentDate) throws InvalidLicenseException {
        if (metadata.getActive() == null || !metadata.getActive()) {
            throw new InvalidLicenseException("License ID is not active.");
        }

        Product productFromLicense = Product.fromValue(metadata.getProduct());
        if (productFromLicense == null || !productFromLicense.equals(expectedProduct)) {
            throw new InvalidLicenseException("Product is not valid. Expected product is: " + expectedProduct + " but license contains: " + productFromLicense);
        }
        if (metadata.getExpirationDate() == null) {
            throw new InvalidLicenseException("License does not contain expiration date.");
        }
        if (!metadata.getExpirationDate().after(currentDate)) {
            throw new InvalidLicenseException("License expired.");
        }
    }
}
