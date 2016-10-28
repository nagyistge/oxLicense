import net.nicholaswilliams.java.licensing.exception.InvalidLicenseException;
import org.testng.annotations.Test;
import org.xdi.oxd.license.client.js.LicenseMetadata;
import org.xdi.oxd.license.client.js.Product;
import org.xdi.oxd.license.validator.LicenseValidator;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 29/08/2016
 */

public class LicenseValidatorMetadataTest {

    @Test
    public void notExpired() {
        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.DAY_OF_MONTH, 10);

        LicenseValidator.validateMetadata(testMetadata(), Product.OXD, new Date());
        LicenseValidator.validateMetadata(testMetadata(), Product.OXD, currentDate.getTime());
    }

    @Test(expectedExceptions = InvalidLicenseException.class)
    public void expired() {
        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.DAY_OF_MONTH, 32);

        LicenseValidator.validateMetadata(testMetadata(), Product.OXD, currentDate.getTime());
    }

    @Test(expectedExceptions = InvalidLicenseException.class)
    public void notValidProduct() {
        LicenseValidator.validateMetadata(testMetadata(), Product.DE, new Date());
    }

    private LicenseMetadata testMetadata() {

        Date creationDate = new Date();

        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.MONTH, 1);

        LicenseMetadata metadata = new LicenseMetadata();
        metadata.setActive(true);
        metadata.setCreationDate(creationDate);
        metadata.setExpirationDate(expiration.getTime());
        metadata.setLicenseId("test_id");
        metadata.setLicenseCountLimit(9999);
        metadata.setProduct("oxd");
        return metadata;
    }
}
