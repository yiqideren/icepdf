/*
 * Copyright 2006-2016 ICEsoft Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icepdf.ri.common.views.annotations.signatures;

import org.icepdf.core.pobjects.acroform.signature.Validator;
import org.icepdf.core.pobjects.annotations.SignatureWidgetAnnotation;
import org.icepdf.ri.common.EscapeJDialog;
import org.icepdf.ri.common.utility.signatures.CertificatePropertiesDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * The SignaturePropertiesDialog shows a signatures basic information; validation, signer info and a summary panel.
 */
public class SignaturePropertiesDialog extends EscapeJDialog {

    private static final Logger logger =
            Logger.getLogger(SignaturePropertiesDialog.class.toString());

    // layouts constraint
    private GridBagConstraints constraints;

    private Validator validator;
    protected static ResourceBundle messageBundle;
    protected SignatureWidgetAnnotation signatureWidgetAnnotation;

    public SignaturePropertiesDialog(Dialog parent, ResourceBundle messageBundle,
                                     SignatureWidgetAnnotation signatureWidgetAnnotation, Validator validator) {
        super(parent, true);
        SignaturePropertiesDialog.messageBundle = messageBundle;
        this.validator = validator;
        this.signatureWidgetAnnotation = signatureWidgetAnnotation;
        buildUI();
    }

    public SignaturePropertiesDialog(Frame parent, ResourceBundle messageBundle,
                                     SignatureWidgetAnnotation signatureWidgetAnnotation, Validator validator) {
        super(parent, true);
        SignaturePropertiesDialog.messageBundle = messageBundle;
        this.validator = validator;
        this.signatureWidgetAnnotation = signatureWidgetAnnotation;
        buildUI();
    }

    private void buildUI() {

        SignatureValidationStatus signatureValidationStatus =
                new SignatureValidationStatus(messageBundle, signatureWidgetAnnotation, validator);

        JPanel annotationPanel = new JPanel(new GridBagLayout());
        add(annotationPanel, BorderLayout.NORTH);

        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 10, 10, 10);

        // basic signer information
        SignerSummaryPanel signerSummaryPanel =
                new SignerSummaryPanel(signatureValidationStatus, messageBundle, signatureWidgetAnnotation, validator, true);
        addGB(annotationPanel, signerSummaryPanel, 0, 0, 2, 1);

        // Validity summary
        SignatureValidationPanel validityPanel =
                new SignatureValidationPanel(signatureValidationStatus, messageBundle, signatureWidgetAnnotation,
                        validator, false, true);
        addGB(annotationPanel, validityPanel, 0, 1, 2, 1);


        // Signer info
        SignerInfoPanel signerInfoPanel = new SignerInfoPanel(signatureValidationStatus, messageBundle, signatureWidgetAnnotation, validator);
        addGB(annotationPanel, signerInfoPanel, 0, 2, 2, 1);

        // close buttons.
        // simple close
        final JButton closeButton = new JButton(messageBundle.getString(
                "viewer.annotation.signature.validation.dialog.close.button.label"));
        closeButton.setMnemonic(messageBundle.getString("viewer.button.cancel.mnemonic").charAt(0));
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });
        final JButton certPropertiesButton = new JButton(messageBundle.getString(
                "viewer.annotation.signature.properties.dialog.showCertificates.label"));
        final JDialog parent = this;
        certPropertiesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new CertificatePropertiesDialog(parent, messageBundle,
                        validator.getCertificateChain())
                        .setVisible(true);
            }
        });

        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 1.0;
        addGB(annotationPanel, certPropertiesButton, 0, 3, 1, 1);

        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        addGB(annotationPanel, closeButton, 1, 3, 1, 1);

        // pack it up and go.
        getContentPane().add(annotationPanel);
        pack();
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    /**
     * Gridbag constructor helper
     *
     * @param component component to add to grid
     * @param x         row
     * @param y         col
     * @param rowSpan
     * @param colSpan
     */
    private void addGB(JPanel layout, Component component,
                       int x, int y,
                       int rowSpan, int colSpan) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = rowSpan;
        constraints.gridheight = colSpan;
        layout.add(component, constraints);
    }
}