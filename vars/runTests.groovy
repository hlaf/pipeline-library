def call(String environment = '', Map config=null, String label='') {
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
		
		if (config?.compute_coverage) {
		  stashCoverageResult key: label
		}
		
	} finally {
		publishJUnitReport()
	}
}
