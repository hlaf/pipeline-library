package com.emt.steps

@groovy.transform.InheritConstructors
class CheckoutFromGit extends BaseStep {

    def required_parameters = ['repo_url']

    Object execute(Map parameters=[:]) {
        validateParameters(parameters)

        def extensions = [
            [$class: 'LocalBranch', localBranch: '**'],
            [$class: 'WipeWorkspace'],
        ]

        if (parameters.target_dir) {
            extensions.add([$class: 'RelativeTargetDirectory',
                            relativeTargetDir: parameters.target_dir])
        }

        _steps.checkout([$class: 'GitSCM',
            branches: [[name: "*/${parameters.branch}"]],
            doGenerateSubmoduleConfigurations: false,
            extensions: extensions,
            submoduleCfg: [],
            userRemoteConfigs: [
                [
                    credentialsId: parameters.repo_creds,
                    name: 'origin',
                    url: parameters.repo_url]]
        ])
    }
}
