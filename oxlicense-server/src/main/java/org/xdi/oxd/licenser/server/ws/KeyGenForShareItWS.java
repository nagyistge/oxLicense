package org.xdi.oxd.licenser.server.ws;

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
            LOG.trace("Request key generation: PURCHASE_ID: {0}, RUNNING_NO: {1}, PURCHASE_DATE: {2}, PRODUCT_ID: {3}, " +
                    "QUANTITY: {4}, REG_NAME: {5}, RESELLER: {6}, LASTNAME: {7}, FIRSTNAME: {8}, COMPANY: {9} ",
                    new Object[]{purchaseId, runningNumber, purchaseDate, productId, numberOfCopiesOrdered,
                            regName, reseller, lastName, firstName, company});
            LOG.trace("EMAIL: {0}, PHONE: {1}, FAX: {2}, STREET: {3}, " +
                    "CITY: {4}, ZIP: {5}, STATE: {6}, COUNTRY: {7}, ENCODING: {8}, LANGUAGE_ID: {9} ",
                    new Object[]{email, phone, fax, street, city,
                            zip, state, country, encoding, languageId});
            LOG.trace("PROMOTION_NAME: {0}, SUBSCRIPTION_DATE: {1}, START_DATE: {2}, EXPIRY_DATE: {3}, " +
                    "ISO_CODE: {4}, INVOICE: {5}",
                    new Object[]{promotionName, subscriptionDate, startDate, expiryDate, isoCode, invoice});

            final LdapLicenseCrypt crypt = cryptService.generate(regName);
            LOG.trace("Generated crypt object: " + crypt.getDn());

            cryptService.save(crypt);
            LOG.trace("Saved crypt object: " + crypt.getDn());

            LicenseMetadata metadata = new LicenseMetadata()
                    .setShareIt(true)
                    .setCreationDate(new Date())
                    .setExpirationDate(expiration())
                    .setMultiServer(true);

            LdapLicenseId licenseId = licenseIdService.generate(crypt.getDn(), metadata);
            LOG.trace("Generated license ID: " + licenseId.getDn());

            licenseIdService.save(licenseId);
            LOG.trace("Saved license ID: " + crypt.getDn());


            return Response.ok().entity(licenseId.getLicenseId()).build();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    private Date expiration() {
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.YEAR, 1);
        return expiration.getTime();
    }


}
