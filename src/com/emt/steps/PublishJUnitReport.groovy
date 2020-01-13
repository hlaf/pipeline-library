package com.emt.steps

@groovy.transform.InheritConstructors
class PublishJUnitReport extends BaseStep {
	Object execute(Map parameters=[:]) {
		_steps.junit keepLongStdio: true, testResults: '**/test_results.xml'
	}
}
