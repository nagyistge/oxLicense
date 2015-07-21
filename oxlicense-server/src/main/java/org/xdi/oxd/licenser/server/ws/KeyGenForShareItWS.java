package org.xdi.oxd.licenser.server.ws;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xdi.oxd.license.client.js.LdapLicenseCrypt;
import org.xdi.oxd.license.client.js.LdapLicenseId;
import org.xdi.oxd.license.client.js.LicenseMetadata;
import org.xdi.oxd.licenser.server.service.LicenseCryptService;
import org.xdi.oxd.licenser.server.service.LicenseIdService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 19/06/2015
 */

@Path("/rest/shareit")
public class KeyGenForShareItWS {

    private static final Logger LOG = LoggerFactory.getLogger(KeyGenForShareItWS.class);

    @Inject
    LicenseCryptService cryptService;
    @Inject
    LicenseIdService licenseIdService;

    @POST
    @Path("/keygen")
    @Produces({MediaType.TEXT_PLAIN})
    public Response generateKey(
            @FormParam("PURCHASE_ID") Integer purchaseId,
            @FormParam("RUNNING_NO") Integer runningNumber,
            @FormParam("PURCHASE_DATE ") String purchaseDate, // date in DD/MM/YYYY
            @FormParam("PRODUCT_ID") Integer productId,
            @FormParam("QUANTITY") Integer numberOfCopiesOrdered,
            @FormParam("REG_NAME") String regName,
            @FormParam("RESELLER") String reseller,
            @FormParam("LASTNAME") String lastName,
            @FormParam("FIRSTNAME") String firstName,
            @FormParam("COMPANY") String company,
            @FormParam("EMAIL") String email,
            @FormParam("PHONE") String phone,
            @FormParam("FAX") String fax,
            @FormParam("STREET") String street,
            @FormParam("CITY") String city,
            @FormParam("ZIP") String zip,
            @FormParam("STATE") String state,
            @FormParam("COUNTRY") String country,
            @FormParam("ENCODING") String encoding,
            @FormParam("LANGUAGE_ID") String languageId,
            @FormParam("PROMOTION_NAME") String promotionName,
            @FormParam("SUBSCRIPTION_DATE") String subscriptionDate,  // date in DD/MM/YYYY
            @FormParam("START_DATE") String startDate, // date in DD/MM/YYYY
            @FormParam("EXPIRY_DATE") String expiryDate, // date in DD/MM/YYYY
            @FormParam("ISO_CODE") String isoCode,
            @FormParam("INVOICE") String invoice, // Possible values: INVOICE="UNPAID" or "PAID"
            @Context HttpServletRequest httpRequest) {

        try {
            LOG.trace("Request key generation: PURCHASE_ID: " + purchaseId +
                    ", RUNNING_NO: " + runningNumber +
                    ", PURCHASE_DATE: " + purchaseDate +
                    ", PRODUCT_ID: " + productId +
                    ", QUANTITY: " + numberOfCopiesOrdered +
                    ", REG_NAME: " + regName +
                    ", RESELLER: " + reseller +
                    ", LASTNAME: " + lastName +
                    ", FIRSTNAME: " + firstName +
                    ", COMPANY: " + company +
                    ", EMAIL: " + email +
                    ", PHONE: " + phone +
                    ", FAX: " + fax +
                    ", STREET: " + street +
                    ", CITY: " + city +
                    ", ZIP: " + zip +
                    ", STATE: " + state +
                    ", COUNTRY: " + country +
                    ", ENCODING: " + encoding +
                    ", LANGUAGE_ID: " + languageId +
                    ", PROMOTION_NAME: " + promotionName +
                    ", SUBSCRIPTION_DATE: " + subscriptionDate +
                    ", START_DATE: " + startDate +
                    ", EXPIRY_DATE: " + expiryDate +
                    ", ISO_CODE: " + isoCode +
                    ", INVOICE: " + invoice);

            // validation
            if (purchaseId <= 0) {
                LOG.trace("PURCHASE_ID is not valid. PURCHASE_ID: " + purchaseId);
                throwBadRequestException("PURCHASE_ID is not valid.");
            }
            if (Strings.isNullOrEmpty(regName)) {
                LOG.trace("REG_NAME is not valid. REG_NAME: " + regName);
                throwBadRequestException("REG_NAME is not valid.");
            }
            if (productId <= 0) {
                LOG.trace("PRODUCT_ID is not valid. PRODUCT_ID: " + productId);
                throwBadRequestException("PRODUCT_ID is not valid.");
            }

            final LdapLicenseCrypt crypt = cryptService.generate(regName);
            LOG.trace("Generated crypt object: " + regName);

            cryptService.save(crypt);
            LOG.trace("Saved crypt object: " + crypt.getDn());

            LicenseMetadata metadata = new LicenseMetadata()
                    .setShareIt(true)
                    .setShareItPurchaseId(purchaseId)
                    .setShareItRegName(regName)
                    .setCreationDate(new Date())
                    .setExpirationDate(expiration())
                    .setMultiServer(true);

            metadata.setShareItProductId(productId);

            LdapLicenseId licenseId = licenseIdService.generate(crypt.getDn(), metadata);
            LOG.trace("Generated license ID: " + licenseId.getDn());

            licenseIdService.save(licenseId);
            LOG.trace("Saved license ID: " + licenseId.getDn());


            return Response.ok().entity(licenseId.getLicenseId()).build();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    private static void throwBadRequestException(String responseEntity) {
        throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(responseEntity).build());
    }

    private Date expiration() {
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.YEAR, 1);
        return expiration.getTime();
    }


}
