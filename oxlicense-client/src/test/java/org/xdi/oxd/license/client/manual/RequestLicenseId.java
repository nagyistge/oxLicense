package org.xdi.oxd.license.client.manual;

import com.google.common.collect.Lists;
import org.xdi.oxauth.client.OpenIdConfigurationClient;
import org.xdi.oxauth.client.OpenIdConfigurationResponse;
import org.xdi.oxauth.client.TokenClient;
import org.xdi.oxauth.client.TokenResponse;
import org.xdi.oxauth.client.uma.CreateGatService;
import org.xdi.oxauth.client.uma.UmaClientFactory;
import org.xdi.oxauth.model.uma.GatRequest;
import org.xdi.oxauth.model.uma.UmaConfiguration;
import org.xdi.oxauth.model.util.Util;
import org.xdi.oxd.license.client.GenerateWS;
import org.xdi.oxd.license.client.Jackson;
import org.xdi.oxd.license.client.LicenseClient;
import org.xdi.oxd.license.client.MetadataWS;
import org.xdi.oxd.license.client.js.LicenseIdItem;
import org.xdi.oxd.license.client.js.LicenseMetadata;

import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.xdi.oxd.license.client.ClientUtils.executor;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 23/08/2016
 */

public class RequestLicenseId {

    public static final String OP_HOST = "https://idp.gluu.org";
    public static final String OP_WELL_KNOWN = OP_HOST + "/.well-known/openid-configuration";
    public static final String OP_UMA_WELL_KNOWN = OP_HOST + "/.well-known/uma-configuration";

    public static final String AAT_CLIENT_ID = "@!A578.3242.DCA8.432A!0001!1DF4.0E33!0008!93B5.75D5";
    public static final String AAT_CLIENT_SECRET = "8qQjj#J&PZ&m-8KH";

    public static final String LICENSE_SERVER_ENDPOINT = "https://license.gluu.org/oxLicense";
    public static final String REQUIRED_SCOPE = "http://idp.gluu.org/uma/scopes/generateLicenseId";

    public static void main(String[] args) throws Exception {
        String aat = obtainAat();
        String gat = obtainGat(aat);

        List<LicenseIdItem> list = generateWS().generateLicenseId(3, "Bearer " + gat, testMetadata());

        assertTrue(!list.isEmpty());
        System.out.println("Generated License IDs:");
        System.out.println(list);

        LicenseIdItem firstLicenseId = list.get(0);
        System.out.println("First license id: " + firstLicenseId.getLicenseId());

        LicenseMetadata metadata = metadataWS().get(firstLicenseId.getLicenseId());
        System.out.println(Jackson.asJsonSilently(metadata));
        System.out.println("Metadata of license id (" + firstLicenseId.getLicenseId() + ") : " + metadata);
        assertTrue(metadata.getProduct().equals("oxd"));

        metadata.setProduct("de");

        Response update = metadataWS().update("Bearer " + gat, metadata);
        assertTrue(update.getStatus() == 200);

        LicenseMetadata updatedMetadata = metadataWS().get(firstLicenseId.getLicenseId());
        System.out.println("Updated metadata of license id (" + firstLicenseId.getLicenseId() + ") : " + updatedMetadata);
        assertTrue(updatedMetadata.getProduct().equals("de")); // metadata is updated !

    }

    private static GenerateWS generateWS() {
        return LicenseClient.generateWs(LICENSE_SERVER_ENDPOINT, executor());
    }

    private static MetadataWS metadataWS() {
        return LicenseClient.metadataWs(LICENSE_SERVER_ENDPOINT, executor());
    }

    private static LicenseMetadata testMetadata() {
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.add(Calendar.YEAR, 1);

        LicenseMetadata metadata = new LicenseMetadata();
        metadata.setProduct("oxd");
        metadata.setLicenseCountLimit(9999);
        metadata.setCreationDate(new Date());
        metadata.setExpirationDate(expirationDate.getTime());
        return metadata;
    }

    private static String obtainGat(String aat) {
        UmaConfiguration umaConfiguration = UmaClientFactory.instance().createMetaDataConfigurationService(OP_UMA_WELL_KNOWN, executor()).getMetadataConfiguration();

        CreateGatService gatService = UmaClientFactory.instance().createGatService(umaConfiguration, executor());

        GatRequest gatRequest = new GatRequest();
        gatRequest.setScopes(Lists.newArrayList(REQUIRED_SCOPE));
        String gat = gatService.createGAT("Bearer " + aat, "idp.gluu.org", gatRequest).getRpt();

        System.out.println("GAT obtained: " + gat);
        return gat;
    }

    private static String obtainAat() throws URISyntaxException {
        OpenIdConfigurationClient client = new OpenIdConfigurationClient(OP_WELL_KNOWN);
        client.setExecutor(executor());
        OpenIdConfigurationResponse discoveryResponse = client.execOpenIdConfiguration();

        final TokenClient tokenClient = new TokenClient(discoveryResponse.getTokenEndpoint());
        tokenClient.setExecutor(executor());
        final TokenResponse response = tokenClient.execClientCredentialsGrant("openid uma_authorization", AAT_CLIENT_ID, AAT_CLIENT_SECRET);
        if (response != null) {
            if (Util.allNotBlank(response.getAccessToken())) {
                System.out.println("Access token obtained :" + response.getAccessToken());
                return response.getAccessToken();
            } else {
                System.out.println("Token is blank in response.");
            }
        } else {
            System.out.println("No response from TokenClient");
        }
        throw new RuntimeException("Failed to obtain AAT.");
    }

}
