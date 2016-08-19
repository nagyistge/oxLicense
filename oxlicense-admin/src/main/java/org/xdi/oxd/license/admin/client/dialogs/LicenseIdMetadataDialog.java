package org.xdi.oxd.license.admin.client.dialogs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import org.xdi.oxd.license.admin.client.Admin;
import org.xdi.oxd.license.admin.client.Framework;
import org.xdi.oxd.license.client.js.LdapLicenseId;
import org.xdi.oxd.license.client.js.LicenseMetadata;
import org.xdi.oxd.license.client.js.Product;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 06/10/2014
 */

public class LicenseIdMetadataDialog {

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    interface MyUiBinder extends UiBinder<Widget, LicenseIdMetadataDialog> {
    }

    private final DialogBox dialog;

    @UiField
    VerticalPanel dialogContent;
    @UiField
    HTML errorMessage;
    @UiField
    Button okButton;
    @UiField
    Button closeButton;
    @UiField
    TextBox numberOfLicenseIds ;
    @UiField
    HTML numberOfLicenseIdsLabel;
    @UiField
    TextBox licenseName;
    @UiField
    ListBox product;
    @UiField
    DateBox expirationDate;
    @UiField
    TextBox licenseCountLimit;

    private final LdapLicenseId licenseId;
    private final boolean isEditMode;

    public LicenseIdMetadataDialog(LdapLicenseId licenseId) {
        uiBinder.createAndBindUi(this);

        this.licenseId = licenseId;
        this.isEditMode = licenseId != null;

        dialog = Framework.createDialogBox("License Id configuration");
        dialog.setWidget(dialogContent);

        setEditMode();
        numberOfLicenseIds.setValue("1");
        licenseCountLimit.setValue(Integer.toString(LicenseMetadata.DEFAULT_LICENSE_COUNT_LIMIT));

        for (Product p : Product.values()) {
            this.product.addItem(p.getValue(), p.getValue());
        }

        closeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                dialog.hide();
            }
        });
        okButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (validate()) {
                    dialog.hide();
                    onOk();
                }
            }
        });
    }

    private void setEditMode() {
        if (!isEditMode) {
            return;
        }

        numberOfLicenseIds.setVisible(false);
        numberOfLicenseIdsLabel.setVisible(false);

        final LicenseMetadata metadataAsObject = licenseId.getMetadataAsObject();
        if (metadataAsObject != null) {
            product.setSelectedIndex(productIndex(metadataAsObject.getProduct()));
            licenseName.setValue(metadataAsObject.getLicenseName());
            licenseCountLimit.setValue(Integer.toString(metadataAsObject.getLicenseCountLimit()));
            expirationDate.setValue(metadataAsObject.getExpirationDate());
        }
    }

    private int productIndex(String product) {
        for (int i = 0; i < this.product.getItemCount(); i++) {
            if (this.product.getValue(i).equals(product)) {
                return i;
            }
        }
        return 0;
    }

    private void showError(String message) {
        errorMessage.setVisible(true);
        errorMessage.setHTML("<span style='color:red;'>" + message + "</span>");
    }

    public String getSelectedProduct() {
        return this.product.getValue(this.product.getSelectedIndex());
    }

    private boolean validate() {
        errorMessage.setVisible(false);

        final Integer numberOfLicenses = numberOfLicenses();
        final Integer licenseCountLimit = licenseCountLimit();

        if ((numberOfLicenses == null || numberOfLicenses <= 0) && !isEditMode) {
            showError("Unable to parse number of licenses. Please enter integer more then zero.");
            return false;
        }
        if (licenseCountLimit == null || licenseCountLimit < 0) {
            showError("Unable to parse number of license count limit.");
            return false;
        }
        if (Product.fromValue(getSelectedProduct()) == null) {
            showError("Product '" + getSelectedProduct() + "' is not supported. Supported products: " + Product.supportedProductsString());
            return false;
        }
        if (expirationDate.getValue() == null) {
            showError("Expiration date is not set.");
            return false;
        }

        final String licenseName = this.licenseName.getValue();
        if (licenseName == null || licenseName.isEmpty()) {
            showError("Please enter name for license.");
            return false;
        }

        return true;
    }

    public LicenseMetadata licenseMetadata() {
    	LicenseMetadata licenseMetadata = new LicenseMetadata();
    	licenseMetadata.setProduct(getSelectedProduct());
    	licenseMetadata.setLicenseName(licenseName.getValue());
    	licenseMetadata.setProduct(getSelectedProduct());
    	licenseMetadata.setLicenseCountLimit(licenseCountLimit());
    	licenseMetadata.setExpirationDate(expirationDate.getValue());
    	
    	return licenseMetadata;
    }

    public Integer numberOfLicenses() {
        return Admin.parse(numberOfLicenseIds.getValue());
    }

    public Integer licenseCountLimit() {
        return Admin.parse(licenseCountLimit.getValue());
    }

    public void onOk() {
    }

    public void setTitle(String title) {
        dialog.setText(title);
        dialog.setTitle(title);
    }

    public void show() {
        dialog.center();
        dialog.show();
    }

    public LdapLicenseId getLicenseId() {
        if (isEditMode) {
            licenseId.setMetadataAsObject(licenseMetadata());
        }
        return licenseId;
    }
}
