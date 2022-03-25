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
        def missing_files = ChangeSetUtils.getFilesNotInWorkspace(_steps.currentBuild, _steps)

        if (missing_files.size() > 0) {
            return error_helper("The change log contains unmapped files!")
        }

        return ChangeSetUtils.getChangedFiles(_steps.currentBuild).any { 
            it.path =~ /^${name}$/
        }
    }

}
