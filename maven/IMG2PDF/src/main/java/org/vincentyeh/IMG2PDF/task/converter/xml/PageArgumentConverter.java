package org.vincentyeh.IMG2PDF.task.converter.xml;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.vincentyeh.IMG2PDF.pdf.page.PageAlign;
import org.vincentyeh.IMG2PDF.pdf.page.PageArgument;
import org.vincentyeh.IMG2PDF.pdf.page.PageDirection;
import org.vincentyeh.IMG2PDF.pdf.page.PageSize;

import java.util.HashMap;
import java.util.Map;

public class PageArgumentConverter implements Converter {
    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext marshallingContext) {
        PageArgument pageArgument=(PageArgument) o;
        writeNode("align",pageArgument.getAlign().toString(),writer);
        writeNode("size",pageArgument.getSize().toString(),writer);
        writeNode("default-direction",pageArgument.getDirection().toString(),writer);
        writeNode("auto-rotate",pageArgument.getAutoRotate()+"",writer);
    }

    private void writeNode(String node,String value,HierarchicalStreamWriter writer){
        writer.startNode(node);
        writer.setValue(value);
        writer.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext unmarshallingContext) {
        PageArgument.Builder builder=new PageArgument.Builder();

        final Map<String,String> elements = new HashMap<>();
        while (reader.hasMoreChildren()){
            reader.moveDown();
            elements.put(reader.getNodeName(),reader.getValue());
            reader.moveUp();
        }
        builder.setAlign(new PageAlign(elements.get("align")));
        builder.setSize(PageSize.valueOf(elements.get("size")));
        builder.setDirection(PageDirection.valueOf(elements.get("default-direction")));
        builder.setAutoRotate(Boolean.parseBoolean(elements.get("auto-rotate")));
        return builder.build();
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(PageArgument.class);
    }
}
