package com.emt.steps

@groovy.transform.InheritConstructors
class UploadToArtifactRepository extends BaseStep {

    def required_parameters = ['artifact_repo_url', 'artifact_repo_creds']

    Object execute(Map parameters=[:]) {
        validateParameters(parameters)
        String artifact_repo_url = parameters.artifact_repo_url
        String artifact_repo_creds = parameters.artifact_repo_creds

        _steps.withCredentials([
            _steps.usernamePassword(
            credentialsId: artifact_repo_creds,
            usernameVariable: 'TWINE_USERNAME',
            passwordVariable: 'TWINE_PASSWORD')
        ]) { _steps.sh """
	          virtualenv master_venv
	          . master_venv/bin/activate
	          python setup.py sdist
	          \\pip install twine --upgrade
	          twine upload --repository-url ${artifact_repo_url} dist/*
			""" }
    }
}
