package org.xdi.oxd.license.admin.server;

import org.xdi.oxd.license.client.js.LicenseMetadata;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 01/11/2016
 */

public class Tester {

    private Tester() {
    }

    public static LicenseMetadata testMetadata() {
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.add(Calendar.YEAR, 1);

        LicenseMetadata metadata = new LicenseMetadata();
        metadata.setActive(true);
        metadata.setProduct("oxd");
        metadata.setLicenseCountLimit(9999);
        metadata.setCustomerName("test_name");
        metadata.setCreationDate(new Date());
        metadata.setExpirationDate(expirationDate.getTime());
        return metadata;
    }
}
