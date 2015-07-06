package org.xdi.oxd.license.client.js;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 06/10/2014
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LicenseMetadata implements Serializable {

    public static final int DEFAULT_THREAD_COUNT = 9;

    @JsonProperty(value = "thread_count")
    private int threadsCount = DEFAULT_THREAD_COUNT;
    @JsonProperty(value = "multi_server")
    private boolean multiServer;
    @JsonProperty(value = "license_type")
    private LicenseType licenseType;
    @JsonProperty(value = "license_name")
    private String licenseName;
    @JsonProperty(value = "license_features")
    private List<String> licenseFeatures;
    @JsonProperty(value = "creation_date")
    private Date creationDate;
    @JsonProperty(value = "expiration_date")
    private Date expirationDate;
    @JsonProperty(value = "license_count_limit")
    private int licenseCountLimit;
    @JsonProperty(value = "share_it")
    private boolean shareIt;
    @JsonProperty(value = "share_it_purchase_id")
    private int shareItPurchaseId;
    @JsonProperty(value = "share_it_reg_name")
    private String shareItRegName;
    @JsonProperty(value = "share_it_product_id")
    private int shareItProductId;

    public LicenseMetadata() {
    }

    public LicenseMetadata(LicenseType licenseType, boolean multiServer, int threadsCount) {
        this.licenseType = licenseType;
        this.multiServer = multiServer;
        this.threadsCount = threadsCount;
    }

    public int getShareItProductId() {
        return shareItProductId;
    }

    public LicenseMetadata setShareItProductId(int shareItProductId) {
        this.shareItProductId = shareItProductId;
        return this;
    }

    public String getShareItRegName() {
        return shareItRegName;
    }

    public LicenseMetadata setShareItRegName(String shareItRegName) {
        this.shareItRegName = shareItRegName;
        return this;
    }

    public int getShareItPurchaseId() {
        return shareItPurchaseId;
    }

    public LicenseMetadata setShareItPurchaseId(int shareItPurchaseId) {
        this.shareItPurchaseId = shareItPurchaseId;
        return this;
    }

    public boolean isShareIt() {
        return shareIt;
    }

    public LicenseMetadata setShareIt(boolean shareIt) {
        this.shareIt = shareIt;
        return this;
    }

    public List<String> getLicenseFeatures() {
        return licenseFeatures;
    }

    public LicenseMetadata setLicenseFeatures(List<String> licenseFeatures) {
        this.licenseFeatures = licenseFeatures;
        return this;
    }

    public String getLicenseName() {
        return licenseName;
    }

    public LicenseMetadata setLicenseName(String licenseName) {
        this.licenseName = licenseName;
        return this;
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public LicenseMetadata setLicenseType(LicenseType licenseType) {
        this.licenseType = licenseType;
        return this;
    }

    public boolean isMultiServer() {
        return multiServer;
    }

    public LicenseMetadata setMultiServer(boolean multiServer) {
        this.multiServer = multiServer;
        return this;
    }

    public int getThreadsCount() {
        return threadsCount;
    }

    public LicenseMetadata setThreadsCount(int threadsCount) {
        this.threadsCount = threadsCount;
        return this;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public LicenseMetadata setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public LicenseMetadata setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public int getLicenseCountLimit() {
        return licenseCountLimit;
    }

    public LicenseMetadata setLicenseCountLimit(int licenseCountLimit) {
        this.licenseCountLimit = licenseCountLimit;
        return this;
    }

    public LicenseMetadata setShareItProductId(Integer shareItProductId) {
        this.shareItProductId = shareItProductId;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("LicenseMetadata");
        sb.append("{creationDate=").append(creationDate);
        sb.append(", threadsCount=").append(threadsCount);
        sb.append(", multiServer=").append(multiServer);
        sb.append(", licenseType=").append(licenseType);
        sb.append(", licenseName='").append(licenseName).append('\'');
        sb.append(", licenseFeatures=").append(licenseFeatures);
        sb.append(", expirationDate=").append(expirationDate);
        sb.append(", licenseCountLimit=").append(licenseCountLimit);
        sb.append(", shareIt=").append(shareIt);
        sb.append(", shareItPurchaseId=").append(shareItPurchaseId);
        sb.append(", shareItRegName='").append(shareItRegName).append('\'');
        sb.append(", shareItProductId=").append(shareItProductId);
        sb.append('}');
        return sb.toString();
    }
}
