def call(String environment = '') {
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
			master_venv\\Scripts\\activate.bat
			pip install tox --upgrade
			tox ${environment}
			"""
		}
		
		
	} finally {
		publishJUnitReport()
	}
}
