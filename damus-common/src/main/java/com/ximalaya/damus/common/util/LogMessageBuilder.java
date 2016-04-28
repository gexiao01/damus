// 文件名称: OperationHistory.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.common.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class LogMessageBuilder {

    private String message;
    private List<String> parameters = Lists.newArrayList();

    public LogMessageBuilder(String message) {
        this.message = StringUtils.trimToEmpty(message);
    }

    public LogMessageBuilder addParameter(String name, Object value) {
        parameters.add(name + "=" + value);
        return this;
    }

    @Override
    public String toString() {
        if (parameters.isEmpty()) {
            return message;
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append(message);
            builder.append(" Parameters: ");
            builder.append(Joiner.on(",").join(parameters));
            return builder.toString();
        }
    }
}
