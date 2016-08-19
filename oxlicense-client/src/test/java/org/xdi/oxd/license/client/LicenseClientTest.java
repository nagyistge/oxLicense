package org.xdi.oxd.license.client;

import com.google.common.base.Strings;
import junit.framework.Assert;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.xdi.oxauth.client.uma.CreateRptService;
import org.xdi.oxauth.client.uma.UmaClientFactory;
import org.xdi.oxauth.model.uma.UmaConfiguration;
import org.xdi.oxd.license.client.data.LicenseResponse;
import org.xdi.oxd.license.client.js.LicenseMetadata;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
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

    @Parameters({"licenseServerEndpoint"})
    @Test(enabled = false)
    public void generateLicenseIds(String licenseServerEndpoint, String umaDiscoveryEndpoint) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        final GenerateWS generateWS = LicenseClient.generateWs(licenseServerEndpoint);

        UmaConfiguration umaConfiguration = UmaClientFactory.instance().createMetaDataConfigurationService(umaDiscoveryEndpoint, trustAllExecutor()).getMetadataConfiguration();

        CreateRptService rptService = UmaClientFactory.instance().createRequesterPermissionTokenService(umaConfiguration, trustAllExecutor());
        String rpt = rptService.createRPT("aat", "https://idp.gluu.org").getRpt();

        List<LicenseResponse> list = generateWS.generateLicenseId(5, "Bearer " + rpt, testMetadata());

        Assert.assertTrue(!list.isEmpty());
        System.out.println(list);
    }

    private static LicenseMetadata testMetadata() {
        LicenseMetadata metadata = new LicenseMetadata();
        metadata.setProduct("oxd");
        metadata.setLicenseCountLimit(9999);
        metadata.setCreationDate(new Date());
        return metadata;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        LicenseClientTest test = new LicenseClientTest();
        test.generateLicenseIds("https://license.gluu.org/oxLicense", "https://idp.gluu.org/.well-known/uma-configuration");
    }

    public static ApacheHttpClient4Executor trustAllExecutor() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        return new ApacheHttpClient4Executor(createHttpClientTrustAll());
    }

    public static HttpClient createHttpClientTrustAll() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        //        System.setProperty("javax.net.debug", "SSL,handshake,trustmanager");

        //        SSLSocketFactory sf = new SSLSocketFactory(new TrustStrategy() {
        //            @Override
        //            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        //                return true;
        //            }
        //        }, new AllowAllHostnameVerifier());

        SSLSocketFactory sf = new SSLSocketFactory(new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        }, new X509HostnameVerifier() {
            @Override
            public void verify(String host, SSLSocket ssl) throws IOException {
            }

            @Override
            public void verify(String host, X509Certificate cert) throws SSLException {
            }

            @Override
            public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
            }

            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        }
        );

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        registry.register(new Scheme("https", 443, sf));
        ClientConnectionManager ccm = new PoolingClientConnectionManager(registry);
        return new DefaultHttpClient(ccm);
    }
}
