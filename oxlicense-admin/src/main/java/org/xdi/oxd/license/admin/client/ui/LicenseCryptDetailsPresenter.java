package org.xdi.oxd.license.admin.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import org.xdi.oxd.license.admin.client.Admin;
import org.xdi.oxd.license.admin.client.SuccessCallback;
import org.xdi.oxd.license.admin.client.dialogs.LicenseIdMetadataDialog;
import org.xdi.oxd.license.admin.client.dialogs.TextAreaDialog;
import org.xdi.oxd.license.client.js.LdapLicenseCrypt;
import org.xdi.oxd.license.client.js.LdapLicenseId;
import org.xdi.oxd.license.client.js.LicenseMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 25/09/2014
 */

public class LicenseCryptDetailsPresenter {

    private final MultiSelectionModel<LdapLicenseId> selectionModel = new MultiSelectionModel<LdapLicenseId>();

    private final LicenseCryptDetailsPanel view;

    private LdapLicenseCrypt licenseCrypt;
    private LicenseCryptTabPresenter parent;
    private LdapLicenseId toSelect;

    public LicenseCryptDetailsPresenter(LicenseCryptDetailsPanel view, LicenseCryptTabPresenter parent) {
        this.view = view;
        this.parent = parent;
        this.view.getGenerateLicenseIdButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                generateLicenseIds();
            }
        });
        this.view.getRemoveButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onRemove();
            }
        });
        this.view.getEditButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onEdit();
            }
        });
        this.view.getRefreshButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                loadLicenseIds();
            }
        });
        this.view.getMonthlyStatisticButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onMonthlyStatisticButton();
            }
        });
        this.view.getCopyIds().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showCopyDialog();
            }
        });
        this.view.getFind().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onFind();
            }
        });

        view.getLicenseIds().setSelectionModel(selectionModel);

        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                setButtonsState();
            }
        });
        setButtonsState();
    }

    private void onFind() {
        TextAreaDialog dialog = new TextAreaDialog() {
            @Override
            public void onOk() {
                find(getTextArea().getValue());
            }
        };
        dialog.getTextArea().setHeight("25px");
        dialog.setTitle("Find by License ID");
        dialog.show();
    }

    private void find(String licenseId) {
        if (Admin.isEmpty(licenseId)) {
            Window.alert("License ID is blank. Please put valid License ID.");
            return;
        }

        Admin.getService().getLicenseId(licenseId, new AsyncCallback<LdapLicenseId>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Failed to find license id.");
            }

            @Override
            public void onSuccess(final LdapLicenseId result) {
                if (result != null) {
                    List<LdapLicenseCrypt> rows = parent.getRows();
                    for (LdapLicenseCrypt crypt : rows) {
                        if (crypt.getDn().equals(result.getLicenseCryptDN())) {
                            toSelect = result;
                            parent.getSelectionModel().setSelected(crypt, true);
                            break;
                        }
                    }


                } else {
                    Window.alert("Failed to find license id.");
                }
            }
        });
    }

    private void onMonthlyStatisticButton() {
        UrlBuilder builder = new UrlBuilder();
        builder.setProtocol(Window.Location.getProtocol());
        builder.setHost(Window.Location.getHost());
        String port = Window.Location.getPort();
        if (port != null && port.length() > 0) {
            builder.setPort(Integer.parseInt(port));
        }

        builder.setPath("/oxLicense/rest/statistic");
        builder.setParameter("licenseId", selectionModel.getSelectedSet().iterator().next().getLicenseId());

        Window.open(builder.buildString(), "_blank", null);
    }

    private void showCopyDialog() {
        TextAreaDialog dialog = new TextAreaDialog();
        dialog.setTitle("License IDs for copy");
        dialog.getTextArea().setValue(licenseIdAsStringForCopy());
        dialog.show();
    }

    private String licenseIdAsStringForCopy() {
        final List<LdapLicenseId> visibleItems = view.getLicenseIds().getVisibleItems();
        String s = "";
        for (LdapLicenseId id : visibleItems) {
            s = s + id.getLicenseId() + "\n";
        }
        return s;
    }

    private void onRemove() {
        Admin.getService().remove(selectionModel.getSelectedSet(), new SuccessCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                LicenseCryptDetailsPresenter.this.loadLicenseIds();
            }
        });
    }

    private void onEdit() {
        LicenseIdMetadataDialog dialog = new LicenseIdMetadataDialog(selectionModel.getSelectedSet().iterator().next()) {
            @Override
            public void onOk() {
                Admin.getService().save(getLicenseId(), new SuccessCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        loadLicenseIds();
                    }
                });
            }
        };
        dialog.show();
    }

    private void setButtonsState() {
        this.view.getRemoveButton().setEnabled(!selectionModel.getSelectedSet().isEmpty());
        this.view.getEditButton().setEnabled(selectionModel.getSelectedSet().size() == 1);
        this.view.getMonthlyStatisticButton().setEnabled(selectionModel.getSelectedSet().size() == 1);
    }

    public void show(LdapLicenseCrypt licenseCrypt) {
        this.licenseCrypt = licenseCrypt;
        this.view.getGenerateLicenseIdButton().setEnabled(licenseCrypt != null);
        if (licenseCrypt == null) {
            clear();
            return;
        }

        loadLicenseIds();

        view.getIdField().setHTML(Admin.asHtml(licenseCrypt.getId()));
        view.getNameField().setHTML(Admin.asHtml(licenseCrypt.getName()));
        view.getPrivateKey().setHTML(Admin.asHtml(licenseCrypt.getPrivateKey()));
        view.getPublicKey().setHTML(Admin.asFullHtml(licenseCrypt.getPublicKey()));
        view.getClientPublicKey().setHTML(Admin.asHtml(licenseCrypt.getClientPublicKey()));
        view.getClientPrivateKey().setHTML(Admin.asHtml(licenseCrypt.getClientPrivateKey()));
        view.getPrivatePassword().setHTML(Admin.asHtml(licenseCrypt.getPrivatePassword()));
        view.getPublicPassword().setHTML(Admin.asHtml(licenseCrypt.getPublicPassword()));
        view.getLicensePassword().setHTML(Admin.asHtml(licenseCrypt.getLicensePassword()));
        view.getLicenseIdCount().setHTML("0");
    }

    public void clear() {
        view.getIdField().setHTML("");
        view.getNameField().setHTML("");
        view.getPrivateKey().setHTML("");
        view.getPublicKey().setHTML("");
        view.getClientPublicKey().setHTML("");
        view.getClientPrivateKey().setHTML("");
        view.getPrivatePassword().setHTML("");
        view.getPublicPassword().setHTML("");
        view.getLicensePassword().setHTML("");
        view.getLicenseIdCount().setHTML("0");
        view.getLicenseIds().setRowCount(0);
        view.getLicenseIds().setRowData(new ArrayList<LdapLicenseId>());
    }


    private void generateLicenseIds() {
        LicenseIdMetadataDialog inputNumberDialog = new LicenseIdMetadataDialog(null) {
            @Override
            public void onOk() {
                generateLicenseIdsImpl(numberOfLicenses(), licenseMetadata());
            }
        };
        inputNumberDialog.show();
    }

    private void generateLicenseIdsImpl(int licenseIdsCount, LicenseMetadata metadata) {
        Admin.getService().generateLicenseIds(licenseIdsCount, licenseCrypt, metadata, new SuccessCallback<List<LdapLicenseId>>() {
            @Override
            public void onSuccess(List<LdapLicenseId> result) {
                loadLicenseIds();
            }
        });
    }

    private void loadLicenseIds() {

        if (licenseCrypt == null) {
            return;
        }

        Admin.getService().loadLicenseIdsByCrypt(licenseCrypt, new SuccessCallback<List<LdapLicenseId>>() {
            @Override
            public void onSuccess(List<LdapLicenseId> result) {
                view.getLicenseIdCount().setHTML(Integer.toString(result.size()));
                view.getLicenseIds().setRowCount(result.size());
                view.getLicenseIds().setRowData(result);

                if (toSelect != null) {
                    for (LdapLicenseId licenseId : result) {
                        if (toSelect.getLicenseId().equals(licenseId.getLicenseId())) {
                            selectionModel.setSelected(licenseId, true);
                        }
                    }
                }
            }
        });
    }


}
