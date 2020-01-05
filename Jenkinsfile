#!groovy

@Library('emt-pipeline-lib@master') _

repo_creds = 'emt-jenkins-git-ssh'
repo_url = 'git@github.com:hlaf/pipeline-library.git'

getPipelineConfig().compute_coverage = true

node('linux') {
   
  stage('commit.checkout') {
	checkoutFromGit(repo_creds, repo_url)
  }

}
