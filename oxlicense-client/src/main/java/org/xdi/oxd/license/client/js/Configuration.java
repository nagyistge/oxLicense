package org.xdi.oxd.license.client.js;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 23/09/2014
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Configuration implements Serializable {

    @JsonProperty(value = "base-dn")
    private String baseDn;
    @JsonProperty(value = "op-host")
    private String opHost;
    @JsonProperty(value = "authorize-request")
    private String authorizeRequest;
    @JsonProperty(value = "logout-url")
    private String logoutUrl;
    @JsonProperty(value = "client-id")
    private String clientId;
    @JsonProperty(value = "uma-pat-client-id")
    private String umaPatClientId;
    @JsonProperty(value = "uma-pat-client-secret")
    private String umaPatClientSecret;
    @JsonProperty(value = "user-access")
    private List<String> userAccess;

    public List<String> getUserAccess() {
        return userAccess;
    }

    public void setUserAccess(List<String> userAccess) {
        this.userAccess = userAccess;
    }

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Configuration");
        sb.append("{baseDn='").append(baseDn).append('\'');
        sb.append(", opHost='").append(opHost).append('\'');
        sb.append(", authorizeRequest='").append(authorizeRequest).append('\'');
        sb.append(", logoutUrl='").append(logoutUrl).append('\'');
        sb.append(", clientId='").append(clientId).append('\'');
        sb.append(", umaPatClientId='").append(umaPatClientId).append('\'');
        sb.append(", umaPatClientSecret='").append(umaPatClientSecret).append('\'');
        sb.append(", userAccess=").append(userAccess);
        sb.append('}');
        return sb.toString();
    }
}

