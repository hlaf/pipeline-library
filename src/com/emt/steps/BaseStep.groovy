package com.emt.steps

abstract class BaseStep implements Serializable {

    protected Object _steps;

    BaseStep(Object executor) {
        _steps = executor
    }

    abstract Object execute(Map args=[:]);

    protected final void validateParameters(Map parameters) {
        for (p_name in required_parameters) {
            if (!parameters.containsKey(p_name)) {
                throw new Exception(
                "The parameter '${p_name}' is mandatory");
            }
        }
    }

}