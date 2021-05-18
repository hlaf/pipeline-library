package com.emt.steps

import groovy.transform.InheritConstructors

@groovy.transform.InheritConstructors
class FileChanged extends BaseStep {
	Object execute(Map parameters=[:]) {
		String name = parameters.name
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
