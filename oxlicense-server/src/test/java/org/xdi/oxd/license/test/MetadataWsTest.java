package org.xdi.oxd.license.test;

import com.google.inject.Inject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import org.xdi.oxd.license.client.js.LdapLicenseCrypt;
import org.xdi.oxd.license.client.js.LdapLicenseId;
import org.xdi.oxd.license.client.js.LicenseMetadata;
import org.xdi.oxd.license.client.js.LicenseType;
import org.xdi.oxd.licenser.server.service.LicenseCryptService;
import org.xdi.oxd.licenser.server.service.LicenseIdService;
import org.xdi.oxd.licenser.server.ws.MetadataWS;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.Date;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created by yuriy on 8/16/2015.
 */
@Guice(modules = org.xdi.oxd.license.test.TestAppModule.class)
public class MetadataWsTest {

    @Inject
    LicenseCryptService licenseCryptService;
    @Inject
    MetadataWS metadataWS;
    @Inject
    LicenseIdService licenseIdService;

    private LdapLicenseId licenseId;
    private Date expirationDate;

    @BeforeClass
    public void setUp() {
        LicenseMetadata metadata = new LicenseMetadata();
        metadata.setLicenseType(LicenseType.PAID);
        metadata.setThreadsCount(9);
        metadata.setLicenseName("Test name");
        metadata.setLicenseCountLimit(4);

        expirationDate = new Date();
        metadata.setExpirationDate(expirationDate);

        LdapLicenseCrypt crypt = licenseCryptService.generate();
        licenseCryptService.save(crypt);

        licenseId = licenseIdService.generate(crypt.getDn(), metadata);
        licenseIdService.save(licenseId);
    }

    @Test
    public void metadata() throws IOException {
        LicenseMetadata metadata = metadataWS.metadata(licenseId.getLicenseId());

        // assert metadata corresponds to licenseId
        assertNotNull(metadata);
        assertEquals(metadata.getLicenseName(), "Test name");
        assertEquals(metadata.getExpirationDate(), expirationDate);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void invalidLicenseId() throws IOException {
        metadataWS.metadata("bla-bla");
    }
}
