package org.xdi.oxd.license.test;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 06/07/2015
 */

public class ShareItTest {

    @Test
    public void test() throws Exception {
        String url = "http://localhost:8090/rest/shareit/keygen";

        ClientRequest clientRequest = new ClientRequest(url);
        final ClientResponse response = clientRequest.post();

        Assert.assertNotEquals(response.getStatus(), 404);
        System.out.println(response);
    }
}
