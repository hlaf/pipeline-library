package com.emt.common;

import java.io.Serializable;

public class MissingArgumentException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public MissingArgumentException(String message) {
        super(message);
    }
}
