package org.xdi.oxd.license.validator;

import com.google.common.base.Preconditions;
import org.xdi.oxd.license.client.js.Product;

import java.io.IOException;
import java.util.Date;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 09/09/2016
 */

public class LicenseValidatorExecutor {

    private static final String ARGUMENTS_MESSAGE = "java org.xdi.oxd.license.validator.LicenseValidatorExecutor " +
            "<license> <public key> <public password> <license password> <product (de, oxd)> <current date as milliseconds>";

    /**
     * Required by Docker Edition.
     *
     * @param args args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Validator expects: " + ARGUMENTS_MESSAGE);
        Preconditions.checkArgument(args.length == 6, "Please specify arguments for program as following: " + ARGUMENTS_MESSAGE);

        String license = args[0];
        String publicKey = args[1];
        String publicPassword = args[2];
        String licensePassword = args[3];
        String product = args[4];
        String currentDate = args[5];
        LicenseValidator.validate(publicKey, publicPassword, licensePassword, license, Product.fromValue(product), new Date(Long.parseLong(currentDate)));
    }

}
