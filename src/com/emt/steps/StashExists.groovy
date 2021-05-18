package com.emt.steps

@groovy.transform.InheritConstructors
class StashExists extends BaseStep {
	Object execute(Map params=[:]) {
      String name = params.name
	  try {
        _steps.tempDir {
		  _steps.unstash name: name
	    }
	  } catch (hudson.AbortException e) {
		return false;
	  }
	  return true;
	}
}
