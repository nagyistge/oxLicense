package org.xdi.oxd.license.client.js;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 06/09/2016
 */

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
    private String uniqueIdentifier;
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

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
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

        return !(uniqueIdentifier != null ? !uniqueIdentifier.equals(that.uniqueIdentifier) : that.uniqueIdentifier != null);

    }

    @Override
    public int hashCode() {
        return uniqueIdentifier != null ? uniqueIdentifier.hashCode() : 0;
    }
}
