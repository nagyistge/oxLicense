package org.xdi.oxd.license.client;

import junit.framework.Assert;
import org.testng.annotations.Test;
import org.xdi.oxd.license.client.js.LicenseMetadata;

import java.io.IOException;
import java.util.Date;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 09/07/2015
 */

public class JacksonTest {

	@Test
	public void metadata() throws IOException {
		LicenseMetadata metadata = new LicenseMetadata();
		metadata.setCreationDate(new Date());
		metadata.setLicenseName("testLicense");
		metadata.setShareIt(true);

		final String json = Jackson.asJson(metadata);
        System.out.println(json);
		final LicenseMetadata deserializedMetadata = Jackson.createJsonMapper().readValue(json, LicenseMetadata.class);

		Assert.assertNotNull(deserializedMetadata);
		Assert.assertEquals(metadata.getLicenseName(), deserializedMetadata.getLicenseName());
	}
}
