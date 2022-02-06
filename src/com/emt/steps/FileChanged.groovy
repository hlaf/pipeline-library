package com.emt.steps

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

        return getChangedFiles(_steps).any { it.path =~ /^${name}$/ }
    }

    def getChangedFiles(context) {
        List changeLogSets = context.currentBuild.changeSets
        List res = []

        changeLogSets.each { change_set ->
            change_set.items.each { entry -> res.addAll(entry.affectedFiles) }
        }
        return res
    }
}
