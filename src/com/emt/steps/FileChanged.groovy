package com.emt.steps

import com.emt.common.ChangeSetUtils

@groovy.transform.InheritConstructors
class FileChanged extends BaseStep {

    def required_parameters = ['name']

    Object execute(Map parameters=[:]) {
        validateParameters(parameters);
        String name = parameters.name

        //def changed_files = new ChangeSetUtils(_steps).getChangeLog()
        def changed_files = _steps.changeSetUtils.getChangeLog()

        if (!_steps.fileExists(name)) {
            return error_helper("The file '${name}' does not exist.")
        }

        return changed_files.any { it =~ /^${name}$/ }
    }

}
