package org.xdi.oxd.license.test;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.xdi.oxd.license.client.ClientUtils;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 06/07/2015
 */

public class ShareItTest {

    @Test
    public void test() throws Exception {
//        String url = "http://localhost:8090/rest/shareit/keygen";
        String url = "https://license.gluu.org/oxLicense/rest/shareit/keygen";

        ClientRequest clientRequest = new ClientRequest(url, new ApacheHttpClient4Executor(ClientUtils.createHttpClientTrustAll()));
        clientRequest.formParameter("PURCHASE_ID", 1);
        clientRequest.formParameter("PRODUCT_ID", 2);
        clientRequest.formParameter("REG_NAME", "testRegName");
        final ClientResponse response = clientRequest.post();

        Assert.assertNotEquals(response.getStatus(), 404);
        System.out.println("Entity: " + response.getEntity(String.class));
    }
}
