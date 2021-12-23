package org.vincentyeh.IMG2PDF.pdf.concrete.objects;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.vincentyeh.IMG2PDF.pdf.framework.objects.PdfDocument;
import org.vincentyeh.IMG2PDF.pdf.framework.objects.PdfPage;
import org.vincentyeh.IMG2PDF.parameter.PDFDocumentInfo;
import org.vincentyeh.IMG2PDF.parameter.Permission;

import java.io.File;
import java.io.IOException;

public class PdfBoxDocumentAdaptor implements PdfDocument<PDDocument> {
    private final PDDocument document;

    private final AccessPermission permission = new AccessPermission();
    private String userPassword;
    private String ownerPassword;

    public PdfBoxDocumentAdaptor(PDDocument document) {
        this.document = document;
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
    public void setInfo(PDFDocumentInfo info) {
        if (info == null) return;

        PDDocumentInformation information = new PDDocumentInformation();
        information.setTitle(info.getTitle());
        information.setAuthor(info.getAuthor());
        information.setSubject(info.getSubject());
        information.setCreator(info.getCreator());
        information.setProducer(info.getProducer());

        document.setDocumentInformation(information);
    }


    @Override
    public void encrypt() throws IOException {
        document.protect(createProtectionPolicy());
    }

    @Override
    public void addPage(PdfPage<?> page) {
        document.addPage(((PdfBoxPageAdaptor) page).get());
    }

    @Override
    public void save(File file) throws IOException {
        document.save(file);
    }

    @Override
    public void close() throws IOException {
        document.close();
    }

    @Override
    public PDDocument get() {
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
