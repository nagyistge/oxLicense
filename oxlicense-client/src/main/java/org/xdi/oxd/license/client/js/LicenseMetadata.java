package org.xdi.oxd.license.client.js;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 06/10/2014
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LicenseMetadata implements Serializable {

	private static final long serialVersionUID = 1248966329399630216L;

	public static final int DEFAULT_THREAD_COUNT = 1;
    public static final int DEFAULT_LICENSE_COUNT_LIMIT = 1;
    public static final boolean DEFAULT_MULTI_SERVER = false;

    @JsonProperty(value = "thread_count")
    private int threadsCount = DEFAULT_THREAD_COUNT;
    @JsonProperty(value = "multi_server")
    private boolean multiServer = DEFAULT_MULTI_SERVER;
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
    private int licenseCountLimit = DEFAULT_LICENSE_COUNT_LIMIT;
    @JsonProperty(value = "share_it")
    private boolean shareIt = false;
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

    public void setShareItProductId(int shareItProductId) {
        this.shareItProductId = shareItProductId;
    }

    public String getShareItRegName() {
        return shareItRegName;
    }

    public void setShareItRegName(String shareItRegName) {
        this.shareItRegName = shareItRegName;
    }

    public int getShareItPurchaseId() {
        return shareItPurchaseId;
    }

    public void setShareItPurchaseId(int shareItPurchaseId) {
        this.shareItPurchaseId = shareItPurchaseId;
    }

    public boolean isShareIt() {
        return shareIt;
    }

    public void setShareIt(boolean shareIt) {
        this.shareIt = shareIt;
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

    public boolean isMultiServer() {
        return multiServer;
    }

    public void setMultiServer(boolean multiServer) {
        this.multiServer = multiServer;
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
