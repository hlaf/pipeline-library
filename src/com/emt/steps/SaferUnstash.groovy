package com.emt.steps

@groovy.transform.InheritConstructors
class SaferUnstash extends BaseStep {

    public static final String METADATA_FILE_NAME = "UNSTASH.meta";

    def required_parameters = ['key', 'to']

    Object execute(Map parameters=[:]) {
        validateParameters(parameters)
        String key = parameters.key
        String unstash_path = parameters.to

        String unstash_metadata_file = "${unstash_path}/${METADATA_FILE_NAME}"

        if (_steps.fileExists(unstash_path)) {
            if (_steps.fileExists(unstash_metadata_file)) {

                def props = _steps.readJSON file: unstash_metadata_file
                if (props.stash_name != key) {
                    return error_helper("Location '${unstash_path}' contains data for stash '${props.stash_name}'")
                }
                if (props.build_number == _steps.env.BUILD_NUMBER) {
                    return
                }
                return error_helper("Location '${unstash_path}' contains data from a prior build")
            }
            return error_helper("Unstash location already exists: '${unstash_path}'")
        }

        _steps.dir(unstash_path) { _steps.unstash key }
        def data = _steps.readJSON text: '{}'
        data.build_number = _steps.env.BUILD_NUMBER
        data.stash_name = key
        _steps.writeAsJson file: unstash_metadata_file, json: data
    }
}
