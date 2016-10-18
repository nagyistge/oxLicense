package org.xdi.oxd.license.test;

import org.xdi.oxd.license.client.js.LicenseMetadata;

import java.util.Calendar;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 23/06/2016
 */

public class TLicenseMetadata {

    public static LicenseMetadata standard() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 12);

        LicenseMetadata metadata = new LicenseMetadata();
        metadata.setProduct("oxd");
        metadata.setLicenseCountLimit(9999);
        metadata.setExpirationDate(calendar.getTime());
        return metadata;
    }
}
