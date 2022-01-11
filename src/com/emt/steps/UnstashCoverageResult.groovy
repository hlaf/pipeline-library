package com.emt.steps

@groovy.transform.InheritConstructors
class UnstashCoverageResult extends BaseStep {
	
	public static final String COVERAGE_RESULTS_BASE_DIR = "coverage_results";
    public static final String COVERAGE_STASH_NAME_PREFIX = "coverage-";
	
	Object execute(Map parameters=[:]) {
		String key = parameters.key

		String unstash_path = "./${COVERAGE_RESULTS_BASE_DIR}/${key}"
		_steps.saferUnstash key: COVERAGE_STASH_NAME_PREFIX + key, to: unstash_path

		return unstash_path
	}
}
