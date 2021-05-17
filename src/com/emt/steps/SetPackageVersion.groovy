package com.emt.steps

import com.emt.common.MissingArgumentException

import groovy.transform.InheritConstructors

@groovy.transform.InheritConstructors
class SetPackageVersion extends BaseStep {

    Object execute(Map parameters=[:]) {
        def required_parameters = ["version", "version_file"]
        for (p_name in required_parameters) {
            if (!parameters.containsKey(p_name)) {
                throw new MissingArgumentException(
                "The parameter '${p_name}' is mandatory");
            }
        }
        
        String version      = parameters.version
        String version_file = parameters.version_file
        String version_key  = parameters.version_key ?: "__version__"
		
		String current_version = new GetPackageVersion(_steps).execute(
									version_file: version_file,
                    				version_key: version_key)
	    String content = _steps.readFile version_file
		String new_content = content.replace(current_version, version)
		_steps.writeFile(file: version_file, text: new_content)
	}

}
