package org.vincentyeh.IMG2PDF.xStream.converter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.junit.Assert;
import org.junit.Test;

public class AccessPermissionConverterTester {

    @Test
    public void setCanAssembleDocument() {
        AccessPermission ap = new AccessPermission();
        ap.setCanAssembleDocument(false);
        verify(ap);
    }

    @Test
    public void setCanExtractContent() {
        AccessPermission ap = new AccessPermission();
        ap.setCanExtractContent(false);
        verify(ap);
    }
    @Test
    public void setCanExtractForAccessibility() {
        AccessPermission ap = new AccessPermission();
        ap.setCanExtractForAccessibility(false);
        verify(ap);
    }
    @Test
    public void setCanFillInForm() {
        AccessPermission ap = new AccessPermission();
        ap.setCanFillInForm(false);
        verify(ap);
    }

    @Test
    public void setCanModify() {
        AccessPermission ap = new AccessPermission();
        ap.setCanModify(false);
        verify(ap);
    }

    @Test
    public void setCanModifyAnnotations() {
        AccessPermission ap = new AccessPermission();
        ap.setCanModifyAnnotations(false);
        verify(ap);
    }

    @Test
    public void setCanPrint() {
        AccessPermission ap = new AccessPermission();
        ap.setCanPrint(false);
        verify(ap);
    }

    @Test
    public void setCanPrintDegraded() {
        AccessPermission ap = new AccessPermission();
        ap.setCanPrintDegraded(false);
        verify(ap);
    }

    private void verify(AccessPermission ap) {
        XStream xStream = initialize();
        String xml = xStream.toXML(ap);
        AccessPermission ap2 = (AccessPermission) xStream.fromXML(xml);
        Assert.assertEquals(ap.getPermissionBytes(), ap2.getPermissionBytes());
    }

    private XStream initialize() {
        XStream xStream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xStream);
        xStream.allowTypes(new Class[]{AccessPermission.class});
        xStream.registerConverter(new AccessPermissionConverter());
        xStream.alias("AccessPermission", AccessPermission.class);
        return xStream;
    }

}