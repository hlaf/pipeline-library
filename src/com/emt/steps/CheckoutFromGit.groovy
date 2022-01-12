package com.emt.steps

@groovy.transform.InheritConstructors
class CheckoutFromGit extends BaseStep {

    def required_parameters = ['repo_url']

    Object execute(Map parameters=[:]) {
        validateParameters(parameters)

        _steps.checkout([$class: 'GitSCM',
            branches: [[name: "*/${parameters.branch}"]],
            doGenerateSubmoduleConfigurations: false,
            extensions: [
                [$class: 'LocalBranch', localBranch: '**'],
                [$class: 'WipeWorkspace']
            ],
            submoduleCfg: [],
            userRemoteConfigs: [
                [
                    credentialsId: parameters.repo_creds,
                    name: 'origin',
                    url: parameters.repo_url]]
        ])
    }
}
