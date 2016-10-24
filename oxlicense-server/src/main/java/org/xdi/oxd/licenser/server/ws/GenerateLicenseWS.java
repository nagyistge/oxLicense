package org.xdi.oxd.licenser.server.ws;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xdi.oxd.license.client.Jackson;
import org.xdi.oxd.license.client.data.LicenseResponse;
import org.xdi.oxd.license.client.js.LdapLicenseCrypt;
import org.xdi.oxd.license.client.js.LdapLicenseId;
import org.xdi.oxd.license.client.js.LdapLicenseIdStatistic;
import org.xdi.oxd.license.client.js.LicenseIdItem;
import org.xdi.oxd.license.client.js.LicenseMetadata;
import org.xdi.oxd.licenser.server.LicenseGenerator;
import org.xdi.oxd.licenser.server.LicenseGeneratorInput;
import org.xdi.oxd.licenser.server.service.ErrorService;
import org.xdi.oxd.licenser.server.service.LicenseCryptService;
import org.xdi.oxd.licenser.server.service.LicenseIdService;
import org.xdi.oxd.licenser.server.service.StatisticService;
import org.xdi.oxd.licenser.server.service.ValidationService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 07/09/2014
 */

@Path("/rest")
public class GenerateLicenseWS {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateLicenseWS.class);

    @Inject
    LicenseGenerator licenseGenerator;
    @Inject
    LicenseIdService licenseIdService;
    @Inject
    LicenseCryptService licenseCryptService;
    @Inject
    ValidationService validationService;
    @Inject
    StatisticService statisticService;

    public LicenseResponse generateLicense(String licenseIdStr, String macAddress) {
        try {
            LdapLicenseId licenseId = validationService.getLicenseId(licenseIdStr);

            LdapLicenseCrypt licenseCrypt = getLicenseCrypt(licenseId.getLicenseCryptDN(), licenseIdStr);

            final LicenseMetadata metadata = Jackson.createJsonMapper().readValue(licenseId.getMetadata(), LicenseMetadata.class);

            if (metadata.getActive() == null || !metadata.getActive()) {
                throw new WebApplicationException(ErrorService.response("License ID is NOT active."));
            }

            int createdLicensesTillNow = statisticService.getAll(licenseIdStr).size();
            if (createdLicensesTillNow >= metadata.getLicenseCountLimit()) {
                LOG.debug("License ID count limit exceeded, licenseId: " + licenseIdStr);
                throw new WebApplicationException(ErrorService.response("License ID count limit exceeded."));
            }

            final Date expiredAt = metadata.getExpirationDate() != null ? metadata.getExpirationDate() : plus30DayFromNow();
            final Date now = new Date();

            if (!now.before(expiredAt)) {
                LOG.debug("License ID is expired, licenseId: " + licenseIdStr + ", expiredAt: " + expiredAt + ", now: " + now);
                throw new WebApplicationException(
                        Response.status(Response.Status.BAD_REQUEST).entity("License ID expires.").
                                build());
            }

            LicenseGeneratorInput input = new LicenseGeneratorInput();
            input.setCreatedAt(now);
            input.setExpiredAt(expiredAt);
            input.setCrypt(licenseCrypt);
            input.setMetadata(licenseId.getMetadata());

            final LicenseResponse licenseResponse = licenseGenerator.generate(input);
            createStatistic(licenseId, macAddress);
            return licenseResponse;
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | IOException e) {
            LOG.error(e.getMessage(), e);
            throw new WebApplicationException(ErrorService.response(e.getMessage()));
        }
    }

    private void createStatistic(LdapLicenseId licenseId, String macAddress) {
        LdapLicenseIdStatistic statistic = new LdapLicenseIdStatistic();
        statistic.setCreationDate(new Date());
        statistic.setMacAddress(Strings.isNullOrEmpty(macAddress) ? null : macAddress);

        statisticService.save(statistic, licenseId.getLicenseId());
    }

    private LdapLicenseCrypt getLicenseCrypt(String licenseCryptDN, String licenseId) {
        try {
            final LdapLicenseCrypt entity = licenseCryptService.get(licenseCryptDN);
            if (entity != null) {
                return entity;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.error("Crypt object is corrupted for License ID: " + licenseId);
        throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("Crypt object is corrupted for License ID: " + licenseId).build());
    }

    private static Date plus30DayFromNow() {
        return new Date(new Date().getTime() + TimeUnit.DAYS.toMillis(30));
    }

    public String generatedLicenseAsString(String licenseId, int count, String macAddress) {
        if (count <= 1) {
            count = 1;
        }

        List<LicenseResponse> list = Lists.newArrayList();
        for (int i = 0; i < count; i++) {
            list.add(generateLicense(licenseId, macAddress));
        }
        return Jackson.asJsonSilently(list);
    }

    @GET
    @Path("/generate")
    @Produces({MediaType.APPLICATION_JSON})
    public Response generateGet(@QueryParam("licenseId") String licenseId, @QueryParam("count") int count, @QueryParam("macAddress") String macAddress) {
        return Response.ok().entity(generatedLicenseAsString(licenseId, count, macAddress)).build();
    }

    @POST
    @Path("/generate")
    @Produces({MediaType.APPLICATION_JSON})
    public Response generatePost(@FormParam("licenseId") String licenseId, @FormParam("count") int count, @FormParam("macAddress") String macAddress) {
        return Response.ok().entity(generatedLicenseAsString(licenseId, count, macAddress)).build();
    }

    @POST
    @Path("/generateLicenseId/{licenseCount}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response generateLicenseIdPost(@PathParam("licenseCount") int licenseCount, LicenseMetadata licenseMetadata) {
        validationService.validate(licenseMetadata);

        if (Strings.isNullOrEmpty(licenseMetadata.getCustomerName())) {
            throw new WebApplicationException(ErrorService.response("'customer_name' attribute is not valid or empty."));
        }

        LdapLicenseCrypt crypt = licenseCryptService.generate(licenseMetadata.getCustomerName());
               licenseCryptService.save(crypt);
        List<LdapLicenseId> licenseIdList = licenseIdService.generateLicenseIdsWithPersistence(licenseCount, crypt, licenseMetadata);

        List<LicenseIdItem> idList = Lists.newArrayList();
        for (LdapLicenseId id : licenseIdList) {
            LicenseIdItem item = new LicenseIdItem();
            item.setLicenseId(id.getLicenseId());
            item.setPublicKey(crypt.getPublicKey());
            item.setPublicPassword(crypt.getPublicPassword());
            item.setLicensePassword(crypt.getLicensePassword());

            idList.add(item);
        }

        String response = Jackson.asJsonSilently(idList);
        LOG.trace(response);
        return Response.ok().entity(idList).build();
    }

    @GET
    @Path("/currentMilliseconds")
    @Produces({MediaType.TEXT_PLAIN})
    public Response currentMilliseconds() {
        return Response.ok().entity(System.currentTimeMillis()).build();
    }

    public static void main(String[] args) throws IOException {
        String m = "{\"product\":\"oxd\",\"emails\":[],\"license_id\":null,\"license_name\"" +
                " :\"testLicense\",\"creation_date\":1476711634,\"expiration_date\":1479303634,\"license" +
                " _count_limit\":999,\"customer_name\":\"test_customer\"}";
        final LicenseMetadata metadata = Jackson.createJsonMapper().readValue(m, LicenseMetadata.class);
        System.out.println(metadata);
    }

}
