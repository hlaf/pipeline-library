package com.emt.common;

import java.util.logging.Handler
import java.util.logging.LogRecord

class CustomHandler extends Handler {

    private Object script;

    public CustomHandler(Object script) {
        this.script = script;
    }

    @Override
    public void publish(LogRecord record) {
        if (isLoggable(record)) {
            this.script.echo(getFormatter().format(record))
        }
    }

    @Override
    public void flush() {}

    @Override
    public void close() throws SecurityException {}

}
