package org.vincentyeh.IMG2PDF.xStream.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.vincentyeh.IMG2PDF.pdf.page.PageAlign;

public class PageAlignConverter implements Converter {
    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter, MarshallingContext marshallingContext) {
        hierarchicalStreamWriter.setValue(o.toString());
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext) {
        return PageAlign.valueOf(hierarchicalStreamReader.getValue());
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(PageAlign.class);
    }
}
