package com.emt.steps

@groovy.transform.InheritConstructors
class CheckoutFromGit extends BaseStep {
	Object execute(Map args) {

		_steps.checkout([$class: 'GitSCM',
			branches: [[name: "*/${args.branch}"]],
			doGenerateSubmoduleConfigurations: false,
			extensions: [[$class: 'LocalBranch', localBranch: '**'],
				         [$class: 'WipeWorkspace']],
			submoduleCfg: [],
			userRemoteConfigs: [[
			  credentialsId: args.repo_creds,
			  name: 'origin',
			  url: args.repo_url]]
		])
	}
}
