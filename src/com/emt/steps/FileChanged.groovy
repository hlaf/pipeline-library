package com.emt.steps

import groovy.transform.InheritConstructors
import com.emt.common.ChangeSetUtils

@groovy.transform.InheritConstructors
class FileChanged extends BaseStep {

    def required_parameters = ['name']

    Object execute(Map parameters=[:]) {
        validateParameters(parameters);
        String name = parameters.name

        if (!_steps.fileExists(name)) {
            return error_helper("The file '${name}' does not exist.")
        }

        return ChangeSetUtils.getChangedFiles(_steps.currentBuild).any { 
            it.path =~ /^${name}$/
        }
    }

}
