package org.vincentyeh.IMG2PDF.task.framework.factory;

import org.vincentyeh.IMG2PDF.task.framework.Task;

public abstract class TaskFactory {
    protected final InitialArgument initialArgument;

    protected TaskFactory(InitialArgument initialArgument) {
        this.initialArgument = initialArgument;
    }

    public abstract Task create(CreateArgument createArgument) throws Exception;

}
