def call(String environment = '') {
	if (environment?.trim()) {
        environment = "-e ${environment}"
	}

	try {
		sh """
	        source master_venv/bin/activate
	
	        # NOTE: Ignore aliases
	        \\pip install tox --upgrade
	
	        tox ${environment}
	    """
	} finally {
		publishJUnitReport()
	}
}
