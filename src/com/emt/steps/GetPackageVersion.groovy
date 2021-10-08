package com.emt.steps

import groovy.transform.InheritConstructors

@groovy.transform.InheritConstructors
class GetPackageVersion extends BaseStep {

    Object execute(Map parameters) {

        String version_file = parameters.version_file
        String version_key  = parameters.version_key ?: "__version__"
		
	    String content = _steps.readFile version_file
	
	    def pattern = /${version_key}\s*=\s*[\'\"]?(\d+(\.\d+)*)[\'\"]?.*/
	    def matcher = content =~ pattern
		if (matcher.size() == 0) {
			_steps.error("Couldn't find version information")
		}
		String version = matcher[0][1]
        return version
	}

}
