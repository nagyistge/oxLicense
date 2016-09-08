package org.xdi.oxd.license.test;

import org.xdi.oxd.license.client.js.LdapLicenseIdStatistic;

import java.util.Date;
import java.util.UUID;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 06/09/2016
 */

public class TLicenseIdStatistic {

    public static LdapLicenseIdStatistic generate(Date creationDate) {
        LdapLicenseIdStatistic item = new LdapLicenseIdStatistic();
        item.setMacAddress(UUID.randomUUID().toString());
        item.setId(UUID.randomUUID().toString());
        item.setCreationDate(creationDate);
        return item;
    }
}
