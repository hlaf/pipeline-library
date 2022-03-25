package com.emt.steps

import java.util.logging.Handler
import java.util.logging.Level
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

import com.emt.common.ChangeSetUtils

import groovy.transform.InheritConstructors

@groovy.transform.InheritConstructors
class FileChanged extends BaseStep {

    def required_parameters = ['name']

    Object execute(Map parameters=[:]) {
        validateParameters(parameters);
        String name = parameters.name

        if (!_steps.fileExists(name)) {
            return error_helper("The file '${name}' does not exist.")
        }

        // Find files that are not mapped to the pipeline workspace
        Collection<String> unmapped_file_paths = ChangeSetUtils.getFilesNotInWorkspace(_steps.currentBuild, _steps)
        if (unmapped_file_paths.size() > 0) {
            return error_helper("The change log contains unmapped files: " + unmapped_file_paths.join(','))
        }

        return ChangeSetUtils.getChangedFiles(_steps.currentBuild).any { 
            it.path =~ /^${name}$/
        }
    }

}
