package org.xdi.oxd.license.client.js;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 24/06/2016
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class LicenseIdItem implements Serializable {

    @JsonProperty(value = "license_id")
    private String licenseId;
    @JsonProperty(value = "license_password")
    private String licensePassword;
    @JsonProperty(value = "public_password")
    private String publicPassword;
    @JsonProperty(value = "public_key")
    private String publicKey;

    public LicenseIdItem() {
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public String getLicensePassword() {
        return licensePassword;
    }

    public void setLicensePassword(String licensePassword) {
        this.licensePassword = licensePassword;
    }

    public String getPublicPassword() {
        return publicPassword;
    }

    public void setPublicPassword(String publicPassword) {
        this.publicPassword = publicPassword;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("LicenseIdItem");
        sb.append("{licenseId='").append(licenseId).append('\'');
        sb.append(", licensePassword='").append(licensePassword).append('\'');
        sb.append(", publicPassword='").append(publicPassword).append('\'');
        sb.append(", publicKey='").append(publicKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
