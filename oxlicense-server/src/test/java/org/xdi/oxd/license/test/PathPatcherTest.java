package org.xdi.oxd.license.test;

import junit.framework.Assert;
import org.testng.annotations.Test;
import org.xdi.oxd.licenser.server.ws.PathPatcher;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 24/06/2016
 */

public class PathPatcherTest {

    @Test
    public void test() {
        assertPatcher("/rest/generateLicenseId/1", "/rest/generateLicenseId");
        assertPatcher("/rest/generateLicenseId/10", "/rest/generateLicenseId");
        assertPatcher("/rest/generateLicenseId/567", "/rest/generateLicenseId");
        assertPatcher("/rest/generateLicenseId", "/rest/generateLicenseId");
    }

    private void assertPatcher(String actual, String expected) {
        Assert.assertEquals(PathPatcher.patchPath(actual), expected);
    }
}
