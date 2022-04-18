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

        def changed_files = ChangeSetUtils.getChangeLog(_steps, this)
        if (this.executionFailed()) {
            return this._execution_error_info;
        }

        return changed_files.any { it.path =~ /^${name}$/ }
    }

}
