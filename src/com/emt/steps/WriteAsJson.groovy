package com.emt.steps

import groovy.json.JsonOutput

@groovy.transform.InheritConstructors
class WriteAsJson extends BaseStep {
    def required_parameters = ["file", "json"]

    Object execute(Map parameters=[:]) {
        validateParameters(parameters)
        String file = parameters.file
        Map json = parameters.json
        _steps.writeFile(file: file, text: JsonOutput.toJson(json))
    }
}
