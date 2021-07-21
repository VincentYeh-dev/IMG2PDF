package org.vincentyeh.IMG2PDF.pdf.parameter;

public interface DocumentArgument {
    String getTitle();
    String getOwnerPassword();
    String getUserPassword();
    Permission getPermission();
    PDFDocumentInformation getInformation();

}
