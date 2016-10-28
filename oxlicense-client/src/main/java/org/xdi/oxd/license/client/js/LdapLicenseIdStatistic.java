package org.xdi.oxd.license.client.js;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 06/09/2016
 */

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.gluu.site.ldap.persistence.annotation.LdapAttribute;
import org.gluu.site.ldap.persistence.annotation.LdapDN;
import org.gluu.site.ldap.persistence.annotation.LdapEntry;
import org.gluu.site.ldap.persistence.annotation.LdapObjectClass;

import java.io.Serializable;
import java.util.Date;

@LdapEntry
@LdapObjectClass(values = {"top", "oxLicenseIdStatistic"})
public class LdapLicenseIdStatistic implements Serializable {

    @LdapDN
    private String dn;
    @LdapAttribute(name = "uniqueIdentifier")
    private String id;
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    @LdapAttribute(name = "oxCreationDate")
    private Date creationDate;
    @LdapAttribute(name = "oxMacAddress")
    private String macAddress;

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LdapLicenseIdStatistic that = (LdapLicenseIdStatistic) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
