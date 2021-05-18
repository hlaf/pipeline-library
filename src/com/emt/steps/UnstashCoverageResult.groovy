package com.emt.steps

@groovy.transform.InheritConstructors
class UnstashCoverageResult extends BaseStep {
	
	public static final String COVERAGE_RESULTS_BASE_DIR = "coverage_results";

    def required_parameters = ['key']
	
	Object execute(Map parameters=[:]) {
        validateParameters(parameters)
		String key = parameters.key

		String unstash_path = "./${COVERAGE_RESULTS_BASE_DIR}/${key}"
		_steps.saferUnstash key: "coverage-${key}", to: unstash_path

		return unstash_path
	}
}
