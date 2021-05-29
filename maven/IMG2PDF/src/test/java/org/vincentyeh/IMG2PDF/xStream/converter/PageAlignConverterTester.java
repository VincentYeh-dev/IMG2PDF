package org.vincentyeh.IMG2PDF.xStream.converter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.junit.Assert;
import org.junit.Test;
import org.vincentyeh.IMG2PDF.pdf.page.PageAlign;

public class PageAlignConverterTester {
    @Test
    public void testEnums() {
        for (PageAlign.VerticalAlign vertical : PageAlign.VerticalAlign.values()) {
            for (PageAlign.HorizontalAlign horizontal : PageAlign.HorizontalAlign.values()) {
                verify(new PageAlign(vertical, horizontal));
            }
        }
    }

    private void verify(PageAlign align) {
        System.out.println(align.toString());
        XStream xStream = initialize();
        String xml = xStream.toXML(align);
        PageAlign align2 = (PageAlign) xStream.fromXML(xml);
        Assert.assertEquals(align.toString(), align2.toString());
    }

    private XStream initialize() {
        XStream xStream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xStream);
        xStream.allowTypes(new Class[]{PageAlign.class});
        xStream.registerConverter(new PageAlignConverter());
        xStream.alias("PageAlign", PageAlign.class);
        return xStream;
    }
}
