package org.vincentyeh.IMG2PDF.task.framework.factory;

import org.vincentyeh.IMG2PDF.task.framework.Task;

import java.util.ArrayList;
import java.util.List;

public abstract class TaskListFactory {
    private final TaskFactory factory;

    protected TaskListFactory(TaskFactory factory) {
        this.factory = factory;
    }

    protected abstract List<CreateArgument> generateArgumentList() throws Exception;

    public final List<Task> create() throws Exception {
        List<Task> tasks = new ArrayList<>();
        for (CreateArgument argument : generateArgumentList()) {
            tasks.add(factory.create(argument));
        }
        return tasks;
    }
}
