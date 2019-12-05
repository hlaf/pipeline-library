def call(results=[]) {
    // Publish the Cobertura test coverage report(s)
	def report_location
	if (results.size() > 0) {
	    for (String k: results) {
		    dir("./coverage_results/${k}") { unstash k }
	    }
		report_location = 'coverage_results/**/coverage.xml'
	} else {
		report_location = '**/coverage.xml'
		archiveArtifacts artifacts: report_location, fingerprint: true
	}

	step([$class: 'CoberturaPublisher', coberturaReportFile: report_location])
}
