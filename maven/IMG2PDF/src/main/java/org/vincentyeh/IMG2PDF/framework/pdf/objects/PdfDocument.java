package org.vincentyeh.IMG2PDF.framework.pdf.objects;

import org.vincentyeh.IMG2PDF.framework.parameter.PDFDocumentInfo;
import org.vincentyeh.IMG2PDF.framework.parameter.Permission;

import java.io.File;
import java.io.IOException;

public interface PdfDocument<DOCUMENT> {
    void setUserPassword(String userPassword);

    void setOwnerPassword(String ownerPassword);

    void setPermission(Permission permission);

    void setInfo(PDFDocumentInfo info);

    void encrypt() throws IOException;

    void addPage(PdfPage<?> page) throws Exception;

    void save(File file) throws IOException;

    void close() throws IOException;

    DOCUMENT get();
}
