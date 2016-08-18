package org.xdi.oxd.license.client.js;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

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

	public static final int DEFAULT_THREAD_COUNT = 9999;
    public static final int DEFAULT_LICENSE_COUNT_LIMIT = 9999;

    @JsonProperty(value = "thread_count")
    private int threadsCount = DEFAULT_THREAD_COUNT;
    @JsonProperty(value = "license_type")
    private LicenseType licenseType;
    @JsonProperty(value = "license_id")
    private String licenseId;
    @JsonProperty(value = "license_name")
    private String licenseName;
    @JsonProperty(value = "license_features")
    private List<String> licenseFeatures = new ArrayList<String>();
    @JsonProperty(value = "creation_date")
    private Date creationDate = new Date();
    @JsonProperty(value = "expiration_date")
    private Date expirationDate;
    @JsonProperty(value = "license_count_limit")
    private int licenseCountLimit = DEFAULT_LICENSE_COUNT_LIMIT;

    public LicenseMetadata() {
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public List<String> getLicenseFeatures() {
        return licenseFeatures;
    }

    public void setLicenseFeatures(List<String> licenseFeatures) {
        this.licenseFeatures = licenseFeatures;
    }

    public String getLicenseName() {
        return licenseName;
    }

    public void setLicenseName(String licenseName) {
        this.licenseName = licenseName;
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(LicenseType licenseType) {
        this.licenseType = licenseType;        
    }

    public int getThreadsCount() {
        return threadsCount;
    }

    public void setThreadsCount(int threadsCount) {
        this.threadsCount = threadsCount;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("LicenseMetadata");
        sb.append("{creationDate=").append(creationDate);
        sb.append(", licenseId=").append(licenseId);
        sb.append(", threadsCount=").append(threadsCount);
        sb.append(", licenseType=").append(licenseType);
        sb.append(", licenseName='").append(licenseName).append('\'');
        sb.append(", licenseFeatures=").append(licenseFeatures);
        sb.append(", expirationDate=").append(expirationDate);
        sb.append(", licenseCountLimit=").append(licenseCountLimit);
        sb.append('}');
        return sb.toString();
    }
}
