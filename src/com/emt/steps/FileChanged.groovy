package com.emt.steps

import java.util.logging.Level
import java.util.logging.Logger

import com.emt.common.ChangeSetUtils
import com.emt.common.CustomHandler

import groovy.transform.InheritConstructors

@groovy.transform.InheritConstructors
class FileChanged extends BaseStep {

    def required_parameters = ['name']

    Object execute(Map parameters=[:]) {
        validateParameters(parameters);
        String name = parameters.name
        def handler = new CustomHandler(_steps);
        handler.setLevel(Level.ALL);
        //Logger.getLogger("com.emt.common.ChangeSetUtils").addHandler(handler);
        Logger.getLogger("").addHandler(handler);

        if (!_steps.fileExists(name)) {
            return error_helper("The file '${name}' does not exist.")
        }

        return ChangeSetUtils.getChangedFiles(_steps.currentBuild).any { 
            it.path =~ /^${name}$/
        }
    }

}
