package org.xdi.oxd.license.test;

import org.xdi.oxd.license.client.js.LicenseMetadata;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 23/06/2016
 */

public class TLicenseMetadata {

    public static LicenseMetadata standard() {
        LicenseMetadata metadata = new LicenseMetadata();
        metadata.setMultiServer(true);
        metadata.setThreadsCount(2);
        metadata.setLicenseCountLimit(9999);
        return metadata;
    }
}
