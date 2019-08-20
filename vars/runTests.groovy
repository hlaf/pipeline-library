def call(String environment = '') {
	if (environment?.trim()) {
        environment = "-e ${environment}"
	}

	try {
		if (System.properties['os.name'].toLowerCase().contains('windows')) {
			bat """
			master_venv/Scripts/activate.bat
			pip install tox --upgrade
			tox ${environment}
			"""
		} else {
			sh """
			source master_venv/bin/activate
			
			# NOTE: Ignore aliases
			\\pip install tox --upgrade
			
			tox ${environment}
			"""
		}
		
		
	} finally {
		publishJUnitReport()
	}
}
