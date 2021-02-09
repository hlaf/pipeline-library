package com.emt.steps

import groovy.json.JsonOutput

@groovy.transform.InheritConstructors
class WriteAsJson extends BaseStep {

	Object execute(Map params=[:]) {
      String file = params.file
      Map json = params.json
      _steps.writeFile(file: file, text: JsonOutput.toJson(json))
	}

}
