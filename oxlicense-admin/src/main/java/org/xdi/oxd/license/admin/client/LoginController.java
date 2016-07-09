package org.xdi.oxd.license.admin.client;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import org.xdi.oxd.license.client.js.Configuration;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 19/10/2014
 */

public class LoginController {

    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());

    private static String TOKEN = null;

    public static void redirectToLoginPage() {
        Admin.getService().getConfiguration(new AsyncCallback<Configuration>() {
            @Override
            public void onFailure(Throwable caught) {
                LOGGER.log(Level.SEVERE, caught.getMessage(), caught);
            }

            @Override
            public void onSuccess(Configuration result) {
                LOGGER.fine("Redirect to:" + result.getAuthorizeRequest());
                Window.Location.assign(result.getAuthorizeRequest());
            }
        });
    }

    public static void logout() {
        TOKEN = null;
        RootLayoutPanel.get().clear();

        Admin.getService().getConfiguration(new AsyncCallback<Configuration>() {
            @Override
            public void onFailure(Throwable caught) {
                LOGGER.log(Level.SEVERE, caught.getMessage(), caught);
                redirectToLoginPage();
            }

            @Override
            public void onSuccess(Configuration result) {
                String url = result.getLogoutUrl() + TOKEN;
                LOGGER.fine("Call end session url: " + url);
                RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
                try {
                    builder.send();
                } catch (RequestException e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
                redirectToLoginPage();
            }
        });
    }

    public static boolean isLoggedIn() {
        return !Admin.isEmpty(TOKEN);
    }

    public static void setToken(String token) {
        TOKEN = token;
    }
}
