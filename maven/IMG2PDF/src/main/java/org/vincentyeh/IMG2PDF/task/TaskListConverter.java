package org.vincentyeh.IMG2PDF.task;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.util.List;

public class TaskListConverter {

    private final XStream xStream;

    public TaskListConverter() {
        xStream = new XStream(new DomDriver());
        xStream.processAnnotations(Task.class);
        xStream.processAnnotations(PageArgument.class);
        xStream.processAnnotations(DocumentArgument.class);
        XStream.setupDefaultSecurity(xStream);
        xStream.allowTypesByWildcard(new String[] {
                "org.vincentyeh.IMG2PDF.**"
        });
    }


    public String toXml(List<Task> tasks) {
        return xStream.toXML(tasks);
    }

    public List<Task> parse(String str) {
        Object tasks=xStream.fromXML(str);
        if (tasks instanceof List)
            return (List<Task>) tasks;
        else throw new RuntimeException("tasks is not a list.");
    }


}
