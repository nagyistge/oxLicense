package org.xdi.oxd.license.admin.client;

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
        LOGGER.fine("Redirecting to login page...");
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
        final String tokenHint = TOKEN;
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
                try {
                    String url = result.getLogoutUrl() + tokenHint;
                    LOGGER.fine("Call end session url: " + url);
                    Window.Location.assign(url);
/**                    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
                    builder.sendRequest("", new RequestCallback() {
                        @Override
                        public void onResponseReceived(Request request, Response response) {
                            redirectToLoginPage();
                        }

                        @Override
                        public void onError(Request request, Throwable exception) {

                        }
                    });*/
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
                //redirectToLoginPage();
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
