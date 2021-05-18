package com.emt.steps

@groovy.transform.InheritConstructors
class PublishCoberturaReport extends BaseStep {
	Object execute(Map parameters=[:]) {
		List results = parameters.get('results', [])
		
		// Publish the Cobertura test coverage report(s)
		def report_location
		if (results.size() > 0) {
			for (String k: results) {
				try {
					_steps.unstashCoverageResult(key: k)
				} catch (hudson.AbortException e) { // Ignore 'No such saved stash'
					println "Warning: ${e.message}" // errors
				}
			}
			report_location = "${UnstashCoverageResult.COVERAGE_RESULTS_BASE_DIR}/**/coverage.xml"
		} else {
			report_location = '**/coverage.xml'
			_steps.archiveArtifacts artifacts: report_location, fingerprint: true
		}

		_steps.step([$class: 'CoberturaPublisher',
                     coberturaReportFile: report_location,
                     onlyStable: false])
	}
}
