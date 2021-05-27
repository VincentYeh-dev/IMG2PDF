package org.vincentyeh.IMG2PDF.xStream.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.vincentyeh.IMG2PDF.task.DocumentArgument;

import java.util.HashMap;
import java.util.Map;

public class DocumentArgumentConverter implements Converter {
    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext marshallingContext) {
        DocumentArgument documentArgument = (DocumentArgument) o;
        writeNode("owner-password", documentArgument.getOwner_password(), writer);
        writeNode("user-password", documentArgument.getUser_password(), writer);

        writer.startNode("AccessPermission");
        writeNode("canPrint", documentArgument.getAp().canPrint() + "", writer);
        writeNode("canModify", documentArgument.getAp().canModify() + "", writer);
        writer.endNode();
    }

    private void writeNode(String node, String value, HierarchicalStreamWriter writer) {
        writer.startNode(node);
        writer.setValue(value);
        writer.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext unmarshallingContext) {
        final Map<String, Object> elements = getElements(reader);
        DocumentArgument.Builder builder = new DocumentArgument.Builder();
        builder.setOwnerPassword(elements.get("owner-password").toString());
        builder.setUserPassword(elements.get("user-password").toString());

        final Map<String, Object> access_permission = (Map<String, Object>) elements.get("AccessPermission");

        AccessPermission ap = new AccessPermission();
        ap.setCanModify(Boolean.parseBoolean(access_permission.get("canModify").toString()));
        ap.setCanPrint(Boolean.parseBoolean(access_permission.get("canPrint").toString()));
        builder.setAccessPermission(ap);

        return builder.build();
    }

    private Map<String, Object> getElements(HierarchicalStreamReader reader) {
        final Map<String, Object> elements = new HashMap<>();
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            if (reader.hasMoreChildren())
                elements.put(reader.getNodeName(), getElements(reader));
            else
                elements.put(reader.getNodeName(), reader.getValue());
            reader.moveUp();
        }
        return elements;
    }


    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(DocumentArgument.class);
    }
}
