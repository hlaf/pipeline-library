import com.emt.steps.CoverageIgnore

@CoverageIgnore
def call(Map parameters=[:]) {

	String environment = parameters.environment ?: ''
	String label = parameters.label ?: ''

	if (environment?.trim()) {
        environment = "-e ${environment}"
	}

	try {
		if (isUnix()) {
			sh """
			source master_venv/bin/activate
			
			# NOTE: Ignore aliases
			\\pip install tox --upgrade
			
			tox ${environment}
			"""
		} else {
			bat """
			${workspace}/master_venv/Scripts/activate.bat && \
			pip install tox --upgrade && \
			tox ${environment}
			"""
		}
		
		if (getPipelineConfig().compute_coverage) {
		  stashCoverageResult key: label
		}
		
	} finally {
		publishJUnitReport()
	}
}
