def call() {
	try {
		sh '''
	        source master_venv/bin/activate
	
	        # NOTE: Ignore aliases
	        \\pip install tox --upgrade
	
	        tox -e coverage
	    '''
	} finally {
		publishJUnitReport()
	}
}
