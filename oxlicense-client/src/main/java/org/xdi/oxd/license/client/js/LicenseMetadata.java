package org.xdi.oxd.license.client.js;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 06/10/2014
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LicenseMetadata implements Serializable {

    public static final int DEFAULT_LICENSE_COUNT_LIMIT = 9999;

    @JsonProperty(value = "active")
    private Boolean active = false;
    @JsonProperty(value = "product")
    private String product;
    @JsonProperty(value = "license_id")
    private String licenseId;
    @JsonProperty(value = "license_name")
    private String licenseName = "";
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    @JsonProperty(value = "creation_date")
    private Date creationDate = new Date();
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    @JsonProperty(value = "expiration_date")
    private Date expirationDate;
    @JsonProperty(value = "license_count_limit")
    private int licenseCountLimit = DEFAULT_LICENSE_COUNT_LIMIT;
    @JsonProperty(value = "customer_name")
    private String customerName = "";
    @JsonProperty(value = "emails")
    private List<String> emails = new ArrayList<>();

    public LicenseMetadata() {
    }

    public Boolean getActive() {
        if (active == null) {
            active = false;
        }
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public String getLicenseName() {
        return licenseName;
    }

    public void setLicenseName(String licenseName) {
        this.licenseName = licenseName;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getLicenseCountLimit() {
        return licenseCountLimit;
    }

    public void setLicenseCountLimit(int licenseCountLimit) {
        this.licenseCountLimit = licenseCountLimit;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("LicenseMetadata");
        sb.append("{creationDate=").append(creationDate);
        sb.append(", licenseId=").append(licenseId);
        sb.append(", active=").append(active);
        sb.append(", product=").append(product);
        sb.append(", licenseName='").append(licenseName).append('\'');
        sb.append(", expirationDate=").append(expirationDate);
        sb.append(", licenseCountLimit=").append(licenseCountLimit);
        sb.append(", customerName=").append(customerName);
        sb.append(", emails=").append(emails);
        sb.append('}');
        return sb.toString();
    }
}
