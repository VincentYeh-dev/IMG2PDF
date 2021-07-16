package org.vincentyeh.IMG2PDF.task.framework.factory;

import org.vincentyeh.IMG2PDF.task.framework.Task;

public interface TaskFactory {
    Task create(Object... objects);
}
