package org.xdi.oxd.license.test;

import com.google.inject.Inject;
import org.testng.annotations.Guice;
import org.xdi.oxd.licenser.server.ws.GenerateLicenseWS;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 23/06/2016
 */
@Guice(modules = org.xdi.oxd.license.test.TestAppModule.class)
public class LicenseIdGeneratorTest {
    @Inject
    GenerateLicenseWS generateLicenseWS;
}
