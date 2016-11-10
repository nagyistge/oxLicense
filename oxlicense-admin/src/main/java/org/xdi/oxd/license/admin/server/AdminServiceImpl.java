package org.xdi.oxd.license.admin.server;

import com.google.common.collect.Lists;
import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xdi.oxd.license.admin.client.service.AdminService;
import org.xdi.oxd.license.admin.shared.IdTokenValidationResult;
import org.xdi.oxd.license.client.ClientUtils;
import org.xdi.oxd.license.client.GenerateWS;
import org.xdi.oxd.license.client.Jackson;
import org.xdi.oxd.license.client.LicenseClient;
import org.xdi.oxd.license.client.js.Configuration;
import org.xdi.oxd.license.client.js.LdapLicenseCrypt;
import org.xdi.oxd.license.client.js.LdapLicenseId;
import org.xdi.oxd.license.client.js.LicenseIdItem;
import org.xdi.oxd.license.client.js.LicenseMetadata;
import org.xdi.oxd.licenser.server.service.LicenseCryptService;
import org.xdi.oxd.licenser.server.service.LicenseIdService;

import java.util.Collection;
import java.util.List;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 23/09/2014
 */

@Singleton
public class AdminServiceImpl extends RemoteServiceServlet implements AdminService {

    private static final Logger LOG = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Inject
    LicenseCryptService licenseCryptService;
    @Inject
    LicenseIdService licenseIdService;
    @Inject
    Configuration conf;

    @Override
    public Configuration getConfiguration() {
        return conf;
    }

    @Override
    public LdapLicenseCrypt generate() {
        return licenseCryptService.generate();
    }

    @Override
    public List<LdapLicenseId> generateLicenseIds(int count, LdapLicenseCrypt licenseCrypt, LicenseMetadata metadata) {
        return licenseIdService.generateLicenseIdsWithPersistence(count, licenseCrypt, metadata);
    }

    @Override
    public List<LdapLicenseId> loadLicenseIdsByCrypt(LdapLicenseCrypt licenseCrypt) {
        return licenseIdService.getByCryptDn(licenseCrypt.getDn());
    }

    @Override
    public LdapLicenseCrypt getLicenseCrypt(String dn) {
        return licenseCryptService.get(dn);
    }

    @Override
    public void save(LdapLicenseId entity) {
        final LicenseMetadata metadataAsObject = entity.getMetadataAsObject();
        if (metadataAsObject != null) {
            entity.setMetadata(Jackson.asJsonSilently(metadataAsObject));
        }

        if (Strings.isNullOrEmpty(entity.getDn())) {
            licenseIdService.save(entity);
        } else {
            licenseIdService.merge(entity);
        }
    }

    @Override
    public LdapLicenseId getLicenseId(String licenseId) {
        return licenseIdService.getById(licenseId);
    }

    @Override
    public boolean isGenerationApiProtected() {
        return isLicenseApiProtected();
    }

    private static boolean isLicenseApiProtected() {
        try {
            List<LicenseIdItem> list = generateWS().generateLicenseId(1, "", Tester.testMetadata());
            if (!list.isEmpty()) {
                LOG.error("SEVERE ERROR : License ID generation endpoint is not protecgted!");
            }
            return list.isEmpty();
        } catch (ClientResponseFailure e) {
            int status = e.getResponse().getStatus();
            return status == 403 || status == 401;
        }
    }

    private static GenerateWS generateWS() {
        return LicenseClient.generateWs(ClientUtils.LICENSE_SERVER_ENDPOINT, ClientUtils.executor());
    }

    public static void main(String[] args) {
        System.out.println(isLicenseApiProtected());
    }

    @Override
    public void save(LdapLicenseCrypt entity) {
        if (Strings.isNullOrEmpty(entity.getDn())) {
            licenseCryptService.save(entity);
        } else {
            licenseCryptService.merge(entity);
        }
    }

    @Override
    public void remove(LdapLicenseCrypt entity) {
        licenseCryptService.remove(entity);
        remove(loadLicenseIdsByCrypt(entity));
    }

    @Override
    public void remove(Collection<LdapLicenseId> entities) {
        for (LdapLicenseId entry : entities) {
            licenseIdService.remove(entry);
        }
    }

    @Override
    public List<LdapLicenseCrypt> getAllLicenseCryptObjects() {
        try {
            return licenseCryptService.getAll();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return Lists.newArrayList();
        }
    }

    @Override
    public IdTokenValidationResult hasAccess(String idToken) {
        return new IdTokenValidator(conf.getUserAccess()).hasAccess(idToken);
    }
}
