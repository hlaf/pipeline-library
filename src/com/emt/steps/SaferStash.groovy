package com.emt.steps

@groovy.transform.InheritConstructors
class SaferStash extends BaseStep {

    def required_parameters = ['name', 'includes']

    Object execute(Map parameters=[:]) {
        validateParameters(parameters)
        if (_steps.stashExists(parameters.name)) {
            return error_helper("The stash '${parameters.name}' already exists")
        }
        _steps.stash name: parameters.name, includes: parameters.includes
    }
}
