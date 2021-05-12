package com.emt.steps

import com.emt.common.MapUtils

@groovy.transform.InheritConstructors
class CheckoutFromBzr extends BaseStep {
	Object execute(Map args=[:]) {
		Map config = MapUtils.merge(_steps.getPipelineConfig(), args)
		if (!config.containsKey('repo_url')) {
			_steps.error('No such key')
		}
		_steps.sh """bzr branch ${config['repo_url']} . --use-existing-dir"""
	}
}
