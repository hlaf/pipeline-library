package com.emt.common;

import java.util.logging.Handler
import java.util.logging.LogRecord

class CustomLogHandler extends Handler {

    private Object script;

    @CoverageIgnoreGenerated
    public CustomHandler(Object script) {
        this.script = script;
    }

    @Override
    @CoverageIgnoreGenerated
    public void publish(LogRecord record) {
        if (isLoggable(record)) {
            this.script.echo(getFormatter().format(record))
        }
    }

    @Override
    @CoverageIgnoreGenerated
    public void flush() {}

    @Override
    @CoverageIgnoreGenerated
    public void close() throws SecurityException {}

}
