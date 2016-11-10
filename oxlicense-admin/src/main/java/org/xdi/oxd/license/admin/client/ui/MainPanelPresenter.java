package org.xdi.oxd.license.admin.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import org.xdi.oxd.license.admin.client.Admin;
import org.xdi.oxd.license.admin.client.LoginController;
import org.xdi.oxd.license.admin.client.Presenter;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 23/09/2014
 */

public class MainPanelPresenter implements Presenter {

//    private static final Logger LOGGER = Logger.getLogger(MainPanelPresenter.class.getName());

    private final MainPanel view = new MainPanel();

    public MainPanelPresenter() {
    }

    @Override
    public void go(HasWidgets.ForIsWidget container) {

        container.clear();
        checkState();

        container.add(view);

        view.getLogoutButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                LoginController.logout();
            }
        });

        new LicenseCryptTabPresenter(view.getLicenseCryptTab());
        Admin.getService().isGenerationApiProtected(new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                view.getApiNotProtectedAlert().setVisible(true);
            }

            @Override
            public void onSuccess(Boolean result) {
                view.getApiNotProtectedAlert().setVisible(!result);
            }
        });
    }

    private void checkState() {
        if (!LoginController.isLoggedIn()) {
            throw new RuntimeException("User is not logged in.");
        }
    }
}
