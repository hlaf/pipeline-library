package com.emt.steps

@groovy.transform.InheritConstructors
class RunTests extends BaseStep {
    def required_parameters = []
    Object execute(Map parameters=[:]) {
        validateParameters(parameters)
        String environment = parameters.environment ?: ''
        String label = parameters.label ?: ''

        if (environment?.trim()) {
            environment = "-e ${environment}"
        }

        try {
            if (_steps.isUnix()) {
                _steps.sh """
			source master_venv/bin/activate
			
			# NOTE: Ignore aliases
			\\pip install tox --upgrade
			
			tox ${environment}
			"""
            } else {
                _steps.bat """
			${_steps.env.WORKSPACE}/master_venv/Scripts/activate.bat && \
			pip install tox --upgrade && \
			tox --recreate ${environment}
			"""
            }

            if (_steps.getPipelineConfig().compute_coverage) {
                _steps.stashCoverageResult key: label
            }
        } finally {
            _steps.publishJUnitReport()
        }
    }
}
