package org.vincentyeh.IMG2PDF.task.converter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.vincentyeh.IMG2PDF.task.Task;
import org.vincentyeh.IMG2PDF.task.converter.xml.DocumentArgumentConverter;
import org.vincentyeh.IMG2PDF.task.converter.xml.PageArgumentConverter;

import java.util.List;

public class TaskListConverter {

    private final XStream xStream;

    public TaskListConverter() {
        xStream = new XStream(new DomDriver());
        xStream.processAnnotations(Task.class);
        xStream.registerConverter(new PageArgumentConverter());
        xStream.registerConverter(new DocumentArgumentConverter());
        XStream.setupDefaultSecurity(xStream);
//        xStream.allowTypes(new Class[]{Task.class, PageArgument.class, DocumentArgument.class});
        xStream.allowTypesByWildcard(new String[] {
                "org.vincentyeh.IMG2PDF.**"
        });
    }


    public String toXml(List<Task> tasks) {
        return xStream.toXML(tasks);
    }

    public List<Task> parse(String str) {
        return (List<Task>) xStream.fromXML(str);
    }


}
