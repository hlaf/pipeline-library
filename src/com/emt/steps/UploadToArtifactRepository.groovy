package com.emt.steps

@groovy.transform.InheritConstructors
class UploadToArtifactRepository extends BaseStep {
	Object execute(Map params=[:]) {
		String artifact_repo_url = params.artifact_repo_url
		String artifact_repo_creds = params.artifact_repo_creds
		
		_steps.withCredentials([_steps.usernamePassword(
			credentialsId: artifact_repo_creds,
			usernameVariable: 'TWINE_USERNAME',
			passwordVariable: 'TWINE_PASSWORD')]) {
			_steps.sh """
	          virtualenv master_venv
	          . master_venv/bin/activate
	          python setup.py sdist
	          \\pip install twine --upgrade
	          twine upload --repository-url ${artifact_repo_url} dist/*
			"""
		}
	}
}
