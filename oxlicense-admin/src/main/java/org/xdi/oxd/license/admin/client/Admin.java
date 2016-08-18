package org.xdi.oxd.license.admin.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import org.xdi.oxd.license.admin.client.service.AdminService;
import org.xdi.oxd.license.admin.client.service.AdminServiceAsync;
import org.xdi.oxd.license.admin.client.ui.MainPanelPresenter;
import org.xdi.oxd.license.admin.shared.IdTokenValidationResult;

import java.util.logging.Logger;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 23/09/2014
 */

public class Admin implements EntryPoint {

    private static final Logger LOGGER = Logger.getLogger(Admin.class.getName());

    private static final EventBus EVENT_BUS = new SimpleEventBus();

    private static final AdminServiceAsync SERVICE = GWT.create(AdminService.class);

    public static SafeHtml asHtml(String str) {
        String s = str != null ? str : "";
        if (s.length() > 40) {
            s = s.substring(0, 40) + "...";
        }
        return SafeHtmlUtils.fromString(s);
    }

    public static SafeHtml asFullHtml(String str) {
        return SafeHtmlUtils.fromSafeConstant(insertPeriodically(str, "<br/>", 40));
    }

    public static String insertPeriodically(String text, String insert, int period) {
        StringBuilder builder = new StringBuilder(
                text.length() + insert.length() * (text.length() / period) + 1);

        int index = 0;
        String prefix = "";
        while (index < text.length()) {
            // Don't put the insert in the very first iteration.
            // This is easier than appending it *after* each substring
            builder.append(prefix);
            prefix = insert;
            builder.append(text.substring(index,
                    Math.min(index + period, text.length())));
            index += period;
        }
        return builder.toString();
    }

    @Override
    public void onModuleLoad() {
        LOGGER.info("started to load module...");

        if (LoginController.isLoggedIn()) {
            MainPanelPresenter mainPanelPresenter = new MainPanelPresenter();
            mainPanelPresenter.go(RootLayoutPanel.get());
        } else {
            RootLayoutPanel.get().add(new Label("Checking state ..."));

            final String idTokenParameter = HashParser.getIdTokenFromHash(Window.Location.getHash());
            LOGGER.fine("idTokenParameter=" + idTokenParameter);
            if (!Admin.isEmpty(idTokenParameter)) {
                getService().hasAccess(idTokenParameter, new AsyncCallback<IdTokenValidationResult>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        LOGGER.fine("Failed to validate id_token.");
                        LoginController.redirectToLoginPage();
                    }

                    @Override
                    public void onSuccess(IdTokenValidationResult result) {
                        LOGGER.fine("id_token validation: " + result);

                        if (result == IdTokenValidationResult.ACCESS_GRANTED) {
                            LoginController.setToken(idTokenParameter);
                            MainPanelPresenter mainPanelPresenter = new MainPanelPresenter();
                            mainPanelPresenter.go(RootLayoutPanel.get());
                        } else {
                            LOGGER.fine("id_token is NOT valid. No rights to access License admin.");
                            RootLayoutPanel.get().clear();
                            RootLayoutPanel.get().add(new Label("Access denied. You are not member of License manager group."));
                            LoginController.setToken(null);
                        }
                    }
                });
            } else {
                LoginController.redirectToLoginPage();
            }
        }
    }

    public static AdminServiceAsync getService() {
        return SERVICE;
    }

    public static EventBus getEventBus() {
        return EVENT_BUS;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().equals("");
    }

    public static Integer parse(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return 0;
        }

    }

}
