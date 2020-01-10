


def call(String git_repo_creds,
         String repo_url,
		 String branch='master') {

  checkout([$class: 'GitSCM',
           branches: [[name: "*/${branch}"]],
           doGenerateSubmoduleConfigurations: false,
		   extensions: [[$class: 'LocalBranch', localBranch: branch], [$class: 'WipeWorkspace']],
		   submoduleCfg: [],
		   userRemoteConfigs: [[
		     credentialsId: git_repo_creds,
		     name: 'origin',
		     url: repo_url]]
   ])

}
