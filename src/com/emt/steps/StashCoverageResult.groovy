package com.emt.steps

import static com.emt.steps.UnstashCoverageResult.COVERAGE_STASH_NAME_PREFIX;

@groovy.transform.InheritConstructors
class StashCoverageResult extends BaseStep {
	Object execute(Map parameters=[:]) {
		String key = parameters.key
		String stash_name = COVERAGE_STASH_NAME_PREFIX + key
		_steps.saferStash name: stash_name, includes: "coverage.xml"
	}
}
