package org.vincentyeh.IMG2PDF.task.framework.factory;

import org.vincentyeh.IMG2PDF.task.framework.Task;

import java.util.ArrayList;
import java.util.List;

public abstract class TaskListFactory {
    protected abstract List<TaskFactory> generateList() throws Exception;

    public final List<Task> create() throws Exception {
        List<Task> tasks = new ArrayList<>();
        for (TaskFactory factory : generateList()) {
            tasks.add(factory.create());
        }
        return tasks;
    }
}
