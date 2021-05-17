package com.emt.steps

@groovy.transform.InheritConstructors
class StashExists extends BaseStep {
    
    def required_parameters = ["name"]
    
    Object execute(Map parameters=[:]) {
        validateParameters(parameters);
        String name = parameters.name
        try {
            _steps.tempDir {
                _steps.unstash name: name
            }
        } catch (hudson.AbortException e) {
            return false;
        }
        return true;
    }
}
