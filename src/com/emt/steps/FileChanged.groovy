package com.emt.steps

import java.util.logging.Handler
import java.util.logging.Level
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

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
        handler.setFormatter(new SimpleFormatter());

        String logger_name = "com.emt.common.ChangeSetUtils"
        
        boolean add_handler = true;
        for (Handler h: Logger.getLogger(logger_name).getHandlers()) {
            if (h instanceof CustomHandler) {
                add_handler = false;
            }
        }

        if (add_handler) {
          Logger.getLogger(logger_name).addHandler(handler);
        }

        if (!_steps.fileExists(name)) {
            return error_helper("The file '${name}' does not exist.")
        }

        return ChangeSetUtils.getChangedFiles(_steps.currentBuild).any { 
            it.path =~ /^${name}$/
        }
    }

}
