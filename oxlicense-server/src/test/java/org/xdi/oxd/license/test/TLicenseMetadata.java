package org.xdi.oxd.license.test;

import org.xdi.oxd.license.client.js.LicenseMetadata;

import java.util.Date;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 23/06/2016
 */

public class TLicenseMetadata {

    public static LicenseMetadata standard() {
        LicenseMetadata metadata = new LicenseMetadata();
        metadata.setExpirationDate(new Date());
        metadata.setMultiServer(true);
        metadata.setThreadsCount(2);
        return metadata;
    }
}
