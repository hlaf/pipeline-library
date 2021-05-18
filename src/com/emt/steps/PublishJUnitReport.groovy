package com.emt.steps

import com.emt.common.MapUtils

@groovy.transform.InheritConstructors
class PublishJUnitReport extends BaseStep {
	Object execute(Map params=[:]) {
        
        Map config = MapUtils.merge(_steps.getPipelineConfig(), params)
        
        String test_results = config.get('test_results', '**/test_results.xml')
		_steps.junit keepLongStdio: true, testResults: test_results
	}
}
