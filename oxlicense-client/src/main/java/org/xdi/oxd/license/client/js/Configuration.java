package org.xdi.oxd.license.client.js;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 23/09/2014
 */

public class Configuration implements Serializable {

    @JsonProperty(value = "base-dn")
    private String baseDn;
    @JsonProperty(value = "op-host")
    private String opHost;
    @JsonProperty(value = "thread-number-paid-license")
    private Integer threadNumberPaidLicense;
    @JsonProperty(value = "thread-number-premium-license")
    private Integer threadNumberPremiumLicense;
    @JsonProperty(value = "authorize-request")
    private String authorizeRequest;
    @JsonProperty(value = "logout-url")
    private String logoutUrl;
    @JsonProperty(value = "client-id")
    private String clientId;
    @JsonProperty(value = "license-possible-features")
    private List<String> licensePossibleFeatures;
    @JsonProperty(value = "uma-pat-client-id")
    private String umaPatClientId;
    @JsonProperty(value = "uma-pat-client-secret")
    private String umaPatClientSecret;

    public String getUmaPatClientId() {
        return umaPatClientId;
    }

    public void setUmaPatClientId(String umaPatClientId) {
        this.umaPatClientId = umaPatClientId;
    }

    public String getUmaPatClientSecret() {
        return umaPatClientSecret;
    }

    public void setUmaPatClientSecret(String umaPatClientSecret) {
        this.umaPatClientSecret = umaPatClientSecret;
    }

    public String getOpHost() {
        return opHost;
    }

    public void setOpHost(String opHost) {
        this.opHost = opHost;
    }

    public List<String> getLicensePossibleFeatures() {
        return licensePossibleFeatures;
    }

    public void setLicensePossibleFeatures(List<String> licensePossibleFeatures) {
        this.licensePossibleFeatures = licensePossibleFeatures;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public String getAuthorizeRequest() {
        return authorizeRequest;
    }

    public void setAuthorizeRequest(String authorizeRequest) {
        this.authorizeRequest = authorizeRequest;
    }

    public Configuration() {
    }

    public String getBaseDn() {
        return baseDn;
    }

    public void setBaseDn(String baseDn) {
        this.baseDn = baseDn;
    }

    public Integer getThreadNumberPaidLicense() {
        return threadNumberPaidLicense;
    }

    public void setThreadNumberPaidLicense(Integer threadNumberPaidLicense) {
        this.threadNumberPaidLicense = threadNumberPaidLicense;
    }

    public Integer getThreadNumberPremiumLicense() {
        return threadNumberPremiumLicense;
    }

    public void setThreadNumberPremiumLicense(Integer threadNumberPremiumLicense) {
        this.threadNumberPremiumLicense = threadNumberPremiumLicense;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Configuration");
        sb.append("{baseDn='").append(baseDn).append('\'');
        sb.append(", threadNumberPaidLicense=").append(threadNumberPaidLicense);
        sb.append(", threadNumberPremiumLicense=").append(threadNumberPremiumLicense);
        sb.append(", opHost=").append(opHost);
        sb.append(", umaPatClientId=").append(umaPatClientId);
        sb.append(", umaPatClientSecret=").append(umaPatClientSecret);
        sb.append('}');
        return sb.toString();
    }
}

