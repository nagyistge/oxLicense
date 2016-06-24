package org.xdi.oxd.license.test;

import com.google.inject.Inject;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import org.xdi.oxd.license.client.js.LicenseMetadata;
import org.xdi.oxd.licenser.server.model.LicenseIdItem;
import org.xdi.oxd.licenser.server.ws.GenerateLicenseWS;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertTrue;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 23/06/2016
 */
@Guice(modules = org.xdi.oxd.license.test.TestAppModule.class)
public class LicenseIdGeneratorTest {

    @Inject
    GenerateLicenseWS generateLicenseWS;

    @Test
    public void generateLicenseWS() throws IOException {
        int licenseCount = 9;
        LicenseMetadata metadata = TLicenseMetadata.standard();
        Response response = generateLicenseWS.generateLicenseIdPost(licenseCount, metadata);
        System.out.println("Response entity: " + response.getEntity());

        List<LicenseIdItem> idList = (List<LicenseIdItem>) response.getEntity();
        assertTrue(!idList.isEmpty());
        assertTrue(idList.size() == licenseCount);

    }
}
