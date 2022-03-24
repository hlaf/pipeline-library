package com.emt.common;

import java.util.logging.Handler
import java.util.logging.LogRecord

class CustomHandler extends Handler implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Override
    public void publish(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        sb.append(record.getMillis())
          .append(" - ")
          .append(record.getSourceClassName())
          .append("#")
          .append(record.getSourceMethodName())
          .append(" - ")
          .append(record.getMessage());
        System.out.println(sb.toString());
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
    
}
