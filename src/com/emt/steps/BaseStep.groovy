package com.emt.steps

import com.emt.common.CoverageIgnoreGenerated
import com.emt.common.ExecutionError

abstract class BaseStep implements Serializable {

    protected Object _steps;
    protected boolean _execution_error = false;
    protected ExecutionError _execution_error_info = null;

    BaseStep(Object executor) {
        _steps = executor
    }

    abstract Object execute(Map args=[:]);

    protected final void validateParameters(Map parameters) {
        ValidationHelper.validateParameters(this, parameters);
    }

    protected final ExecutionError error_helper(String message) {
        _execution_error = true;
        ErrorHelper.error(_steps, message);
        _execution_error_info = new ExecutionError(message);
        return _execution_error_info;
    }

    public final boolean executionFailed() { return _execution_error; }

}

@CoverageIgnoreGenerated
class ErrorHelper {
  protected static void error(Object steps, String message) {
    if (Boolean.getBoolean("com.emt.unit_testing_enabled") == false) {
      steps.error(message)
    }
  }
}

@CoverageIgnoreGenerated
class ValidationHelper {
  protected static void validateParameters(BaseStep step,
                                           Map parameters) {
      for (p_name in step.required_parameters) {
          if (!parameters.containsKey(p_name)) {
			  Throwable e = new RuntimeException("The parameter '${p_name}' is mandatory");
			  e.setStackTrace(new StackTraceElement[0]);
              throw e;
          }
      }
  }
}
