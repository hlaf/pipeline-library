#!groovy

@Library('emt-pipeline-lib@master') _

repo_creds = 'emt-jenkins-git-ssh'
repo_url = 'git@github.com:hlaf/pipeline-library.git'

getPipelineConfig().compute_coverage = true

node('linux') {
   
  stage('commit.checkout') {
	checkoutFromGit(repo_creds, repo_url)
  }

  stage('commit.tests.unit') {
	try {
	  sh """
	  chmod +x gradlew
	  ./gradlew test
	  """
	} finally {
	  junit keepLongStdio: true, testResults: '**/build/test-results/test/TEST-*.xml'
	}
  }

}
