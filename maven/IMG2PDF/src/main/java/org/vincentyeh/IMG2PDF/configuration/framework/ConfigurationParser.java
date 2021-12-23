package org.vincentyeh.IMG2PDF.configuration.framework;

import java.util.Map;

public interface ConfigurationParser {
    enum ConfigParam {
        DIR_LIST_READ_CHARSET, LOCALE
    }
    <T> Map<ConfigParam,Object> parse(T parameter);
}
