package com.emt.steps

import com.emt.common.CoverageIgnoreGenerated

abstract class BaseStep implements Serializable {

    protected Object _steps;
    protected boolean _called_error = false;

    BaseStep(Object executor) {
        _steps = executor
    }

    abstract Object execute(Map args=[:]);

    protected final void validateParameters(Map parameters) {
        ValidationHelper.validateParameters(this, parameters);
    }

    protected final Object error_helper(String message) {
        _called_error = true;
        ErrorHelper.error(_steps, message);
    }

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
              throw new RuntimeException("The parameter '${p_name}' is mandatory");
          }
      }
  }
}
