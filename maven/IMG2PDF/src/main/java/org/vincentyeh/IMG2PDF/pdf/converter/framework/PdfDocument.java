package org.vincentyeh.IMG2PDF.pdf.converter.framework;

import org.vincentyeh.IMG2PDF.pdf.parameter.Permission;

import java.io.File;
import java.io.IOException;

public interface PdfDocument<DOCUMENT> {
    void setUserPassword(String userPassword);

    void setOwnerPassword(String ownerPassword);

    void setPermission(Permission permission);

    void setTitle(String title);

    void encrypt() throws IOException;

    void addPage(PdfPage<?> page) throws Exception;

    void save(File file) throws IOException;

    void close() throws IOException;

    DOCUMENT get();
}
