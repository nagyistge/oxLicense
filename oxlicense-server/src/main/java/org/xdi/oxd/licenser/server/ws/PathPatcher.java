package org.xdi.oxd.licenser.server.ws;

import org.apache.commons.lang.StringUtils;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 24/06/2016
 */

public class PathPatcher {

    public static String patchPath(String path) {
        if (path != null) {
            String count = StringUtils.substringAfterLast(path, "/");
            if (isNumber(count)) {
                return StringUtils.substringBeforeLast(path, "/");
            }
        }
        return path;
    }

    private static boolean isNumber(String count) {
        try {
            Integer.parseInt(count);
            return true;
        } catch (Exception e) {
            //ignore
        }
        return false;
    }
}
