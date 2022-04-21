package com.emt.steps

import com.cloudbees.groovy.cps.NonCPS

@groovy.transform.InheritConstructors
class CheckoutFromGit extends BaseStep {

    def required_parameters = ['repo_url']

    Object execute(Map parameters=[:]) {
        validateParameters(parameters)

        _steps.checkout([$class: 'GitSCM',
            branches: [[name: "*/${parameters.branch}"]],
            doGenerateSubmoduleConfigurations: false,
            extensions: getExtensions(parameters),
            submoduleCfg: [],
            userRemoteConfigs: [
                [
                    credentialsId: parameters.repo_creds,
                    name: 'origin',
                    url: parameters.repo_url]]
        ])
    }
    
    @NonCPS
    private def getExtensions(Map parameters) {
        def extensions = [
            [$class: 'LocalBranch', localBranch: '**'],
            [$class: 'WipeWorkspace'],
        ]

        if (parameters.target_dir) {
            extensions.add([$class: 'RelativeTargetDirectory',
                            relativeTargetDir: parameters.target_dir])
        }
        return extensions
    }
    
}


