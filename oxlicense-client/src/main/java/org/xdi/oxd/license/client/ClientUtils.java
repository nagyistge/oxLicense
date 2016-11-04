package org.xdi.oxd.license.client;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 06/07/2015
 */

public class ClientUtils {

    public static final String LICENSE_SERVER_ENDPOINT = "https://license.gluu.org/oxLicense";

    private ClientUtils() {
    }

    public static ClientExecutor executor() {
           return new ApacheHttpClient4Executor(createHttpClientTrustAll());
       }

       public static HttpClient createHttpClientTrustAll() {
           try {
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
               ClientConnectionManager ccm = new SingleClientConnManager(registry);
               return new DefaultHttpClient(ccm);
           } catch (Exception e) {
               throw new RuntimeException(e);
           }
       }
}
