import com.emt.steps.CoverageIgnore

@CoverageIgnore
def call(String artifact_repo_url,
	     String artifact_repo_creds) {

    withCredentials([usernamePassword(credentialsId: artifact_repo_creds,
		        	 usernameVariable: 'TWINE_USERNAME',
			         passwordVariable: 'TWINE_PASSWORD')]) {
		sh """
          virtualenv master_venv
          . master_venv/bin/activate
          python setup.py sdist
          \\pip install twine --upgrade
          twine upload --repository-url ${artifact_repo_url} dist/*
		"""
	}

}
