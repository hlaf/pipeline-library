package com.emt.steps

@groovy.transform.InheritConstructors
class NotifyBuild extends BaseStep {
    def required_parameters = ['build_status', 'recipient_email']
    Object execute(Map parameters=[:]) {
        validateParameters(parameters)

        String build_status = parameters.build_status
        String recipient_email = parameters.recipient_email

        _steps.emailext (
                to: recipient_email,
                subject: "${build_status}: ${_steps.env.JOB_NAME} - Build # ${_steps.env.BUILD_NUMBER}",
                body: """${_steps.env.JOB_NAME} - Build # ${_steps.env.BUILD_NUMBER}

Check console output at ${_steps.env.BUILD_URL} to view the results.""",
                )
    }
}
