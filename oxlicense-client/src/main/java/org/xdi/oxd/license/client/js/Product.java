package org.xdi.oxd.license.client.js;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 19/08/2016
 */

public enum Product {
    DE("de"), OXD("oxd");

    private final String value;

    private Product(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Product fromValue(String value) {
        for (Product product : values()) {
            if (product.getValue().equals(value)) {
                return product;
            }
        }
        return null;
    }

    public static String supportedProductsString() {
        String result = "";
        for (Product product : Product.values()) {
            result += product.getValue() + " ";
        }
        return result.trim();
    }
}
