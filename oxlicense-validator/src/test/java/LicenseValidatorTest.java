import com.google.common.base.Preconditions;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.xdi.oxd.license.client.js.Product;
import org.xdi.oxd.license.validator.LicenseValidator;

import java.io.IOException;
import java.util.Date;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 27/05/2015
 */

public class LicenseValidatorTest {

    private static final String ARGUMENTS_MESSAGE = "java org.xdi.oxd.license.validator.LicenseValidator " +
            "<license> <public key> <public password> <license password> <product: oxd or de> <current date as milliseconds in java.util.Date>";

    @Parameters({"publicKey", "publicPassword", "licensePassword", "license"})
    @Test(enabled = false)
    public void test(String publicKey, String publicPassword, String licensePassword, String license, String product, String currentTimeInMiliseconds) throws IOException {
        LicenseValidator.validate(publicKey, publicPassword, licensePassword, license, Product.fromValue(product), new Date(Long.parseLong(currentTimeInMiliseconds)));
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Validator expects: " + ARGUMENTS_MESSAGE);
        Preconditions.checkArgument(args.length == 6, "Please specify arguments for program as following: " + ARGUMENTS_MESSAGE);

        String license = args[0];
        String publicKey = args[1];
        String publicPassword = args[2];
        String licensePassword = args[3];
        String product = args[4];
        String currentTimeInMilliseconds = args[5];
        LicenseValidator.validate(publicKey, publicPassword, licensePassword, license, Product.fromValue(product), new Date(Long.parseLong(currentTimeInMilliseconds)));
    }

}
