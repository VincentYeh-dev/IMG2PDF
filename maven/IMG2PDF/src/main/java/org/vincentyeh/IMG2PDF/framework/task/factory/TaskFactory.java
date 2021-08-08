package org.vincentyeh.IMG2PDF.framework.task.factory;

import org.vincentyeh.IMG2PDF.framework.task.Task;

public interface TaskFactory<SOURCE> {
    Task create(SOURCE source) throws Exception;
}
