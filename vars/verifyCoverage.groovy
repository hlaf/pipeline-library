def call() {
    
    // Copy the latest successful build's coverage.xml
	def previous_build = getLatestSuccessfulBuildWithArtifacts()
	
	try {
		copyArtifacts(projectName: "${env.JOB_NAME}",
			          selector: specific("${previous_build.number}"),
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
    def branch_rate_old = new BigDecimal(coverage_old['@branch-rate']).setScale(2, java.math.RoundingMode.HALF_UP)
    def line_rate_old = new BigDecimal(coverage_old['@line-rate']).setScale(2, java.math.RoundingMode.HALF_UP)
    
    def xml_new = readFile "${env.WORKSPACE}/coverage.xml"
    def coverage_new = new XmlParser().parseText(xml_new)
    def branch_rate_new = new BigDecimal(coverage_new['@branch-rate']).setScale(2, java.math.RoundingMode.HALF_UP)
    def line_rate_new = new BigDecimal(coverage_new['@line-rate']).setScale(2, java.math.RoundingMode.HALF_UP)
    
	echo "Current coverage metrics:"
	echo "  Branch rate: ${coverage_old['@branch-rate']}"
	echo "  Line rate  : ${coverage_old['@line-rate']}"
	
	echo "New build's coverage metrics:"
	echo "  Branch rate: ${coverage_new['@branch-rate']}"
	echo "  Line rate  : ${coverage_new['@line-rate']}"

	// Fail the job if coverage decreases
	if (branch_rate_new < branch_rate_old || line_rate_new < line_rate_old) {
		error "Code coverage decreased from build ${previous_build.number} to build ${env.BUILD_NUMBER}"
	}

}

@NonCPS
def getLatestSuccessfulBuildWithArtifacts() {
	b = currentBuild
	while (b.getPreviousBuild() != null) {
		b = b.getPreviousBuild()
		if (b.getRawBuild().result == Result.SUCCESS && b.getRawBuild().getHasArtifacts()) {
			return b;
		}
	};
	return null;
}

