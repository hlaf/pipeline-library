import com.cloudbees.groovy.cps.NonCPS

def call(Map parameters=[:]) {
    
	String key = parameters.key ?: ''

    // Copy the latest successful build's coverage.xml
	def previous_build = getLatestSuccessfulBuildWithArtifacts()
	def default_baseline_coverage = '0.85'
	def branch_rate_old
	def line_rate_old
	
	if (previous_build == null) {
		echo "Couldn't find prior coverage results - using the default baseline for new projects"

		branch_rate_old = new BigDecimal(default_baseline_coverage).setScale(2, java.math.RoundingMode.HALF_UP)
		line_rate_old = new BigDecimal(default_baseline_coverage).setScale(2, java.math.RoundingMode.HALF_UP)

	} else {
		copyArtifacts(projectName: "${env.JOB_NAME}",
				      selector: specific("${previous_build.number}"),
			          target: 'previous_build', filter: '**/coverage.xml',
			          flatten: true);

        echo "Reading ${env.WORKSPACE}/previous_build/coverage.xml"
		def xml_old = readFile "${env.WORKSPACE}/previous_build/coverage.xml"
		def coverage_old = new XmlParser().parseText(xml_old)
		branch_rate_old = new BigDecimal(coverage_old['@branch-rate']).setScale(2, java.math.RoundingMode.HALF_UP)
		line_rate_old = new BigDecimal(coverage_old['@line-rate']).setScale(2, java.math.RoundingMode.HALF_UP)
	}

	String results_path_new = unstashCoverageResult(key: key)
	String xml_path_new = "${results_path_new}/coverage.xml"
    echo "Reading ${xml_path_new}"
    def xml_new = readFile xml_path_new
    def coverage_new = new XmlParser().parseText(xml_new)
    def branch_rate_new = new BigDecimal(coverage_new['@branch-rate']).setScale(2, java.math.RoundingMode.HALF_UP)
    def line_rate_new = new BigDecimal(coverage_new['@line-rate']).setScale(2, java.math.RoundingMode.HALF_UP)
    
	echo "Baseline coverage metrics:"
	echo "  Branch rate: ${branch_rate_old}"
	echo "  Line rate  : ${line_rate_old}"
	
	echo "Current build's coverage metrics:"
	echo "  Branch rate: ${branch_rate_new}"
	echo "  Line rate  : ${line_rate_new}"

	// Fail the job if coverage decreases
	if (branch_rate_new < branch_rate_old || line_rate_new < line_rate_old) {
		if (previous_build != null) {
			error "Code coverage decreased from build ${previous_build.number} to build ${env.BUILD_NUMBER}"
		} else {
			error "Code coverage is too low"
		}
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

