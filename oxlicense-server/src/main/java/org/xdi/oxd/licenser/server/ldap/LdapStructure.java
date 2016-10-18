package org.xdi.oxd.licenser.server.ldap;

import com.google.inject.Inject;
import org.xdi.oxd.license.client.js.Configuration;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 30/09/2014
 */

public class LdapStructure {

    private Configuration conf;

    @Inject
    public LdapStructure(Configuration conf) {
        this.conf = conf;
    }

    public String getBaseDn() {
        return conf.getBaseDn();
    }

    public String getCustomerBaseDn() {
        return ou(getCustomerOu()) + conf.getBaseDn();
    }

    public String getCustomerOu() {
        return "customer";
    }

    public String getLicenseIdOu() {
        return "licenseId";
    }

    public String getLicenseCryptOu() {
        return "licenseCrypt";
    }

    public String getLicenseCryptBaseDn() {
        return ou(getLicenseCryptOu()) + conf.getBaseDn();
    }

    public String getLicenseIdBaseDn() {
        return ou(getLicenseIdOu()) + conf.getBaseDn();
    }

    public String getStatisticBaseDn(String licenseId) {
        return "licenseId=" + licenseId + "," + ou(getLicenseIdOu()) + conf.getBaseDn();
    }

    private static String ou(String ou) {
        return String.format("ou=%s,", ou);
    }

}
