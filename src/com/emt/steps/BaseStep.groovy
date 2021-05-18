package com.emt.steps

import com.emt.common.MissingArgumentException

abstract class BaseStep implements Serializable {

    protected Object _steps;

    BaseStep(Object executor) {
        _steps = executor
    }

    abstract Object execute(Map args=[:]);

    protected final void validateParameters(Map parameters) {
        for (p_name in required_parameters) {
            if (!parameters.containsKey(p_name)) {
                throw new MissingArgumentException(
                "The parameter '${p_name}' is mandatory");
            }
        }
    }
}
