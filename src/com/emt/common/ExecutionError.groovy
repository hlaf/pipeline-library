package com.emt.common

class ExecutionError implements Serializable {
    private String message;
    public ExecutionError(String message) { this.message = message; }
    public getMessage() { return message; }
}
