package com.emt.common;

import java.util.logging.Handler
import java.util.logging.LogRecord

class CustomHandler extends Handler implements Serializable {

    private static final long serialVersionUID = 2L;
    private transient Object _steps;

    public CustomHandler(Object steps) {
        _steps = steps;
    }
    
    @Override
    public void publish(LogRecord record) {
        if (isLoggable(record)) {
            StringBuilder sb = new StringBuilder();
            sb.append(record.getLevel())
              .append(" - ")
              .append(record.getMessage());
            _steps.echo(sb.toString());
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
    
}
