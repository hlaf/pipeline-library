package com.emt.steps

@groovy.transform.InheritConstructors
class TempDir extends BaseStep {

    def required_parameters = ["body"]

    Object execute(Map parameters=[:]) {
        validateParameters(parameters)

        Closure body = parameters.body
        def result
        _steps.dir(_steps.pwd(tmp: true)) {
            result = body()
            _steps.deleteDir()
        }
        return result
    }
}
