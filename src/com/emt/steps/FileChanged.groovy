package com.emt.steps

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

        Logger logger = Logger.getLogger("com.emt.common.ChangeSetUtils");
        logger.addHandler(new CustomHandler(_steps));

        if (!_steps.fileExists(name)) {
            return error_helper("The file '${name}' does not exist.")
        }

        return ChangeSetUtils.getChangedFiles(_steps.currentBuild).any { 
            it.path =~ /^${name}$/
        }
    }

}
