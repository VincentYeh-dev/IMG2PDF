package org.vincentyeh.IMG2PDF.pdf.converter.concrete;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.PdfDocumentBuilder;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.PdfPage;
import org.vincentyeh.IMG2PDF.pdf.parameter.Permission;

import java.io.File;
import java.io.IOException;

public class PDFBoxDocumentBuilder extends PdfDocumentBuilder {

    private final PDDocument document;

    private final AccessPermission permission = new AccessPermission();
    private String userPassword;
    private String ownerPassword;

    public PDFBoxDocumentBuilder(long maxMainMemoryBytes, File tempFolder) {
        this.document = new PDDocument(MemoryUsageSetting.setupMixed(maxMainMemoryBytes).setTempDir(tempFolder));
    }

    @Override
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public void setOwnerPassword(String ownerPassword) {
        this.ownerPassword = ownerPassword;
    }

    @Override
    public void setPermission(Permission permission) {
        this.permission.setCanAssembleDocument(permission.getCanAssembleDocument());
        this.permission.setCanExtractContent(permission.getCanExtractContent());
        this.permission.setCanExtractForAccessibility(permission.getCanExtractForAccessibility());
        this.permission.setCanFillInForm(permission.getCanFillInForm());
        this.permission.setCanModify(permission.getCanModify());
        this.permission.setCanModifyAnnotations(permission.getCanModifyAnnotations());
        this.permission.setCanPrint(permission.getCanPrint());
        this.permission.setCanPrintDegraded(permission.getCanPrintDegraded());
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void encrypt() throws IOException {
        document.protect(createProtectionPolicy());
    }

    @Override
    public void addPage(PdfPage<?> page) {
        document.addPage((PDPage) page.get());
    }

    public final PDDocument getResult() {
        return document;
    }

    private StandardProtectionPolicy createProtectionPolicy() {
        // Define the length of the encryption key.
        // Possible values are 40 or 128 (256 will be available in PDFBox 2.0).
        int keyLength = 128;
        StandardProtectionPolicy spp = new StandardProtectionPolicy(ownerPassword, userPassword, permission);
        spp.setEncryptionKeyLength(keyLength);
        return spp;
    }
}
