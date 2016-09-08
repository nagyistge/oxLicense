package org.xdi.oxd.license.test;

import com.google.inject.Inject;
import junit.framework.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import org.xdi.oxd.license.client.js.LdapLicenseIdStatistic;
import org.xdi.oxd.license.client.js.LicenseIdItem;
import org.xdi.oxd.license.client.js.LicenseMetadata;
import org.xdi.oxd.licenser.server.ldap.LdapStructure;
import org.xdi.oxd.licenser.server.service.LicenseIdService;
import org.xdi.oxd.licenser.server.service.StatisticService;
import org.xdi.oxd.licenser.server.ws.GenerateLicenseWS;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 06/09/2016
 */
@Guice(modules = org.xdi.oxd.license.test.TestAppModule.class)
public class StatisticServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(StatisticServiceTest.class);

    @Inject
    StatisticService statisticService;
    @Inject
    LicenseIdService licenseIdService;
    @Inject
    GenerateLicenseWS generateLicenseWS;
    @Inject
    LdapStructure ldapStructure;

    private LicenseIdItem licenseId;

    @BeforeClass
    public void setUp() {
        LicenseMetadata metadata = TLicenseMetadata.standard();
        List<LicenseIdItem> licenseIds = (List<LicenseIdItem>) generateLicenseWS.generateLicenseIdPost(1, metadata).getEntity();
        licenseId = licenseIds.get(0);

        Calendar calendar = Calendar.getInstance();
        Date today = new Date();

        calendar.add(Calendar.MONTH, -1);
        Date lastMonth = calendar.getTime();

        calendar.add(Calendar.MONTH, -2);
        Date twoMonthsBefore = calendar.getTime();

        calendar.add(Calendar.MONTH, -3);
        Date treeMonthsBefore = calendar.getTime();

        generate(today);
        generate(lastMonth);
        generate(twoMonthsBefore);
        generate(treeMonthsBefore);

        LOG.trace("LicenseId: " + licenseId.getLicenseId());
    }

    private LdapLicenseIdStatistic generate(Date creationDate) {
        LdapLicenseIdStatistic saved = statisticService.save(TLicenseIdStatistic.generate(creationDate), licenseId.getLicenseId());

        LdapLicenseIdStatistic fetched = statisticService.get(saved.getDn());
        Assert.assertNotNull(fetched);
        return fetched;
    }

    @AfterClass
    public void tearDown() {
        licenseIdService.removeWithSubtreeByLicenseId(licenseId.getLicenseId());
    }

    @Test
    public void loadByPeriod() throws IOException {
        Calendar calendar = Calendar.getInstance();
        Date today = new Date();

        calendar.add(Calendar.MONTH, -12);
        Date minus12Months = calendar.getTime();

        List<LdapLicenseIdStatistic> forPeriod = statisticService.getForPeriod(licenseId.getLicenseId(), minus12Months, today);
        assertEquals(forPeriod.size(), 4);

        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -2);
        Date minus2Months = calendar.getTime();
        forPeriod = statisticService.getForPeriod(licenseId.getLicenseId(), minus2Months, today);
        assertEquals(forPeriod.size(), 2);

        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date minus1Months = calendar.getTime();
        forPeriod = statisticService.getForPeriod(licenseId.getLicenseId(), minus1Months, today);
        assertEquals(forPeriod.size(), 1);
    }

    @Test
    public void monthlyStatistic() throws IOException {
        generate(new Date());
        LdapLicenseIdStatistic last = generate(new Date());
        last.setDn(null);
        last.setId(UUID.randomUUID().toString());

        statisticService.save(last, licenseId.getLicenseId());

        String json = statisticService.monthlyStatistic(licenseId.getLicenseId());
        LOG.info(json);
        assertTrue(json.contains("mac_address"));
    }
}
