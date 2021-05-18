package com.emt.steps

@groovy.transform.InheritConstructors
class StashCoverageResult extends BaseStep {

    def required_parameters = ['key']

    Object execute(Map parameters=[:]) {
        validateParameters(parameters)
        String key = parameters.key
        String stash_name = "coverage-${key}"
        _steps.saferStash name: stash_name, includes: "coverage.xml"
    }
}
