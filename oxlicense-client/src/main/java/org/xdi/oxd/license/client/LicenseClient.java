package org.xdi.oxd.license.client;

import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ProxyFactory;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 07/09/2014
 */

public class LicenseClient {

    private LicenseClient() {
    }

    public static GenerateWS generateWs(String endpoint) {
        return proxy(GenerateWS.class, endpoint);
    }

    public static GenerateWS generateWs(String endpoint, ClientExecutor clientExecutor) {
        return proxy(GenerateWS.class, endpoint, clientExecutor);
    }

    public static MetadataWS metadataWs(String endpoint) {
        return proxy(MetadataWS.class, endpoint);
    }

    public static MetadataWS metadataWs(String endpoint, ClientExecutor clientExecutor) {
        return proxy(MetadataWS.class, endpoint, clientExecutor);
    }

    public static <T> T proxy(Class<T> clientInterface, String endpoint) {
        return ProxyFactory.create(clientInterface, endpoint);
    }

    public static <T> T proxy(Class<T> clientInterface, String endpoint, ClientExecutor clientExecutor) {
        return ProxyFactory.create(clientInterface, endpoint, clientExecutor);
    }

}
