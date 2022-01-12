package com.emt.steps

@groovy.transform.InheritConstructors
class TempDir extends BaseStep {
    Object execute(Map parameters=[:]) {

        def required_parameters = ["body"]
        for (p_name in required_parameters) {
            if (!parameters.containsKey(p_name)) {
                throw new Exception(
                "The parameter '${p_name}' is mandatory");
            }
        }

        Closure body = parameters.body
        def result
        _steps.dir(_steps.pwd(tmp: true)) {
            result = body()
            _steps.deleteDir()
        }
        return result
    }
}
