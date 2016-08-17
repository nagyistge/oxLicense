package org.xdi.oxd.license.client;

import com.google.common.base.Strings;
import junit.framework.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.xdi.oxd.license.client.data.LicenseResponse;

import java.util.List;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 07/09/2014
 */

public class LicenseClientTest {

    @Parameters({"licenseServerEndpoint"})
    @Test(enabled = false)
    public void generateLicense(String licenseServerEndpoint) {
        final GenerateWS generateWS = LicenseClient.generateWs(licenseServerEndpoint);

        final List<LicenseResponse> generatedLicense = generateWS.generatePost("id");

        Assert.assertTrue(generatedLicense != null && !generatedLicense.isEmpty() && !Strings.isNullOrEmpty(generatedLicense.get(0).getEncodedLicense()));
        System.out.println(generatedLicense.get(0).getEncodedLicense());
    }
}
