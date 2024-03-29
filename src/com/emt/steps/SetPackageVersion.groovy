package com.emt.steps

import groovy.transform.InheritConstructors

@groovy.transform.InheritConstructors
class SetPackageVersion extends BaseStep {

    def required_parameters = ['version', 'version_file']

    Object execute(Map parameters=[:]) {
        validateParameters(parameters)

        String version      = parameters.version
        String version_file = parameters.version_file
        String version_key  = parameters.version_key ?: "__version__"

        BaseStep delegate = new GetPackageVersion(_steps);
        String current_version = delegate.execute(
                version_file: version_file,
                version_key: version_key)
        if (delegate.executionFailed()) {
            return error_helper(delegate._execution_error_info.getMessage())
        }

        String content = _steps.readFile version_file
        String new_content = content.replace(current_version, version)
        _steps.writeFile(file: version_file, text: new_content)
    }
}
