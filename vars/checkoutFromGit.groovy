def call(String git_repo_creds,
         String repo_url) {

  checkout([$class: 'GitSCM',
           branches: [[name: '*/master']],
           doGenerateSubmoduleConfigurations: false,
		   extensions: [[$class: 'LocalBranch', localBranch: 'master'], [$class: 'WipeWorkspace']],
		   submoduleCfg: [],
		   userRemoteConfigs: [[
		     credentialsId: git_repo_creds,
		     name: 'origin',
		     url: repo_url]]
   ])

}
