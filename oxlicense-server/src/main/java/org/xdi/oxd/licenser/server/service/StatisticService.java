package org.xdi.oxd.licenser.server.service;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.util.StaticUtils;
import org.codehaus.jettison.json.JSONObject;
import org.gluu.site.ldap.persistence.LdapEntryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xdi.oxd.license.client.js.Configuration;
import org.xdi.oxd.license.client.js.LdapLicenseIdStatistic;
import org.xdi.oxd.licenser.server.ldap.LdapStructure;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 06/09/2016
 */

public class StatisticService {

    private static final Logger LOG = LoggerFactory.getLogger(LicenseIdService.class);

    @Inject
    LdapEntryManager ldapEntryManager;
    @Inject
    Configuration conf;
    @Inject
    LdapStructure ldapStructure;
    @Inject
    ValidationService validationService;

    public void merge(LdapLicenseIdStatistic entity) {
        try {
            ldapEntryManager.merge(entity);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public LdapLicenseIdStatistic save(LdapLicenseIdStatistic entity, String licenseId) {
        try {
            setDnIfEmpty(entity, licenseId);
            ldapEntryManager.persist(entity);
            return entity;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    private void setDnIfEmpty(LdapLicenseIdStatistic entity, String licenseId) {
        if (Strings.isNullOrEmpty(entity.getId())) {
            entity.setId(UUID.randomUUID().toString());
        }

        if (Strings.isNullOrEmpty(entity.getDn())) {
            entity.setDn(dn(entity.getId(), licenseId));
        }
    }

    private String dn(String id, String licenseId) {
        return "uniqueIdentifier=" + id + "," + ldapStructure.getStatisticBaseDn(licenseId);
    }

    public LdapLicenseIdStatistic get(String dn) {
        return ldapEntryManager.find(LdapLicenseIdStatistic.class, dn);
    }

    public List<LdapLicenseIdStatistic> getAll(String licenseId) {
        try {
            Filter filter = Filter.create("&(uniqueIdentifier=*)");
            return ldapEntryManager.findEntries(ldapStructure.getStatisticBaseDn(licenseId), LdapLicenseIdStatistic.class, filter);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    public List<LdapLicenseIdStatistic> getFiltered(String licenseId, Predicate<LdapLicenseIdStatistic> predicate) {
        List<LdapLicenseIdStatistic> filtered = Lists.newArrayList();
        List<LdapLicenseIdStatistic> all = getAll(licenseId);
        for (LdapLicenseIdStatistic item : all) {
            if (predicate.apply(item)) {
                filtered.add(item);
            }
        }

        LOG.trace("ALL size: " + all.size() + ", filtered size: " + filtered.size());
        return filtered;
    }

    public List<LdapLicenseIdStatistic> getForPeriod(String licenseId, Date from, Date to) {
        try {
            String filter = String.format("&(uniqueIdentifier=*)(oxCreationDate>=%s)(oxCreationDate<=%s)",
                    StaticUtils.encodeGeneralizedTime(from),
                    StaticUtils.encodeGeneralizedTime(to));
            LOG.trace("period filter: " + filter);
            return ldapEntryManager.findEntries(ldapStructure.getStatisticBaseDn(licenseId), LdapLicenseIdStatistic.class, Filter.create(filter));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    public String lastHoursStatistic(String licenseId, int hours) {
        validationService.getLicenseId(licenseId);
        validationService.validateHours(hours);

        try {
            final Calendar inPast = Calendar.getInstance();
            inPast.add(Calendar.HOUR, -hours);

            List<LdapLicenseIdStatistic> filtered = getFiltered(licenseId, new Predicate<LdapLicenseIdStatistic>() {
                @Override
                public boolean apply(LdapLicenseIdStatistic input) {
                    //LOG.trace("Result: " + input.getCreationDate().before(inPast.getTime()) + ", creation: " + input.getCreationDate() + ", inPast: " + inPast.getTime());
                    return input.getCreationDate().after(inPast.getTime());
                }
            });

            Map<String, Integer> macAddressToCounter = new HashMap<>();
            for (LdapLicenseIdStatistic item : filtered) {
                Integer counter = macAddressToCounter.get(item.getMacAddress());
                if (counter == null) {
                    counter = 1;
                } else {
                    counter++;
                }

                //LOG.trace("macAddressToCounter put, mac: " + item.getMacAddress() + ", counter: " + counter);
                macAddressToCounter.put(item.getMacAddress(), counter);
            }

            LOG.trace("macAddressToCounter size: " + macAddressToCounter.size());

            JSONObject lastHoursStatistic = new JSONObject();

            for (Map.Entry<String, Integer> entity : macAddressToCounter.entrySet()) {
//                JSONObject stat = new JSONObject();
//                stat.put("mac_address", entity.getKey());
//                stat.put("count", entity.getValue());

                lastHoursStatistic.put(entity.getKey(), entity.getValue());
            }

            JSONObject wrapper = new JSONObject();
            wrapper.put("statistic", lastHoursStatistic);
            wrapper.put("total_generated_licenses", filtered.size());
            return wrapper.toString(2);
        } catch (Exception e) {
            LOG.error("Failed to construct statistic for license_id: " + licenseId, e);
            return "{\"error\":\"Failed to construct statistic for license_id: " + licenseId + "\"}";
        }
    }

    public String monthlyStatistic(String licenseId) {
        validationService.getLicenseId(licenseId);

        try {
            List<LdapLicenseIdStatistic> entities = getAll(licenseId);
            Map<String, List<LdapLicenseIdStatistic>> map = constructMonthlyMap(entities);

            JSONObject monthlyStatistic = new JSONObject();

            for (Map.Entry<String, List<LdapLicenseIdStatistic>> entity : map.entrySet()) {
                JSONObject stat = new JSONObject();
                stat.put("license_generated_count", entity.getValue().size());
                stat.put("mac_address", macAddressMap(entity.getValue()));

                monthlyStatistic.put(entity.getKey(), stat);
            }

            JSONObject wrapper = new JSONObject();
            wrapper.put("monthly_statistic", monthlyStatistic);
            wrapper.put("total_generated_licenses", entities.size());
            return wrapper.toString(2);
        } catch (Exception e) {
            LOG.error("Failed to construct statistic for license_id: " + licenseId, e);
            return "{\"error\":\"Failed to construct statistic for license_id: " + licenseId + "\"}";
        }
    }

    private Map<String, Integer> macAddressMap(List<LdapLicenseIdStatistic> value) {
        Map<String, Integer> map = Maps.newHashMap();
        for (LdapLicenseIdStatistic v : value) {
            if (Strings.isNullOrEmpty(v.getMacAddress())) {
                continue;
            }

            Integer counter = map.get(v.getMacAddress());
            if (counter == null) {
                map.put(v.getMacAddress(), 1);
            } else {
                counter++;
                map.put(v.getMacAddress(), counter);
            }
        }
        return map;
    }

    private Map<String, List<LdapLicenseIdStatistic>> constructMonthlyMap(List<LdapLicenseIdStatistic> entities) {
        Map<String, List<LdapLicenseIdStatistic>> map = Maps.newTreeMap();

        for (LdapLicenseIdStatistic s : entities) {
            String key = yearAndMonth(s.getCreationDate());
            List<LdapLicenseIdStatistic> monthList = map.get(key);
            if (monthList == null) {
                monthList = Lists.newArrayList();
                map.put(key, monthList);
            }
            monthList.add(s);
        }
        return map;
    }

    private String yearAndMonth(Date date) {
        Calendar creationDate = Calendar.getInstance();
        creationDate.setTime(date);

        int month = creationDate.get(Calendar.MONTH) + 1;
        int year = creationDate.get(Calendar.YEAR);
        return year + "-" + month;
    }
}
