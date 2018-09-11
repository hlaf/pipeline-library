def call() {
    
    // Copy the latest successful build's coverage.xml
	try {
		copyArtifacts(projectName: "${env.JOB_NAME}", selector: lastSuccessful(),
					  target: 'previous_build', filter: '**/coverage.xml',
			          flatten: true);
	} catch (Exception e) {
		if (e.message.contains('Failed to copy artifacts')) {
			echo "Couldn't find prior coverage results - assuming new project"
			return
		}
		throw e
	}
    
    def xml_old = readFile "${env.WORKSPACE}/previous_build/coverage.xml"
    def coverage_old = new XmlParser().parseText(xml_old)
    def branch_rate_old = coverage_old['@branch-rate']
    def line_rate_old = coverage_old['@line-rate']
    
    def xml_new = readFile "${env.WORKSPACE}/coverage.xml"
    def coverage_new = new XmlParser().parseText(xml_new)
    def branch_rate_new = coverage_new['@branch-rate']
    def line_rate_new = coverage_new['@line-rate']
    
	def run_wrapper = selectRun job: "${env.JOB_NAME}",
								selector: permalink('lastSuccessfulBuild')
	def latest_build = run_wrapper.getNumber()

	// Fail the job if coverage decreases
	if (branch_rate_new < branch_rate_old || line_rate_new < line_rate_old) {
		error "Code coverage decreased from build ${latest_build} to build ${env.BUILD_NUMBER}"
	}

}
