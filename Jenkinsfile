#!groovy

@Library('emt-pipeline-lib@master') _

repo_creds = 'emt-jenkins-git-ssh'
repo_url = 'git@github.com:hlaf/pipeline-library.git'

getPipelineConfig().compute_coverage = true

node('docker-slave') {
   
  stage('commit.checkout') {
	checkoutFromGit(repo_creds, repo_url)
  }

  stage('commit.tests.unit') {
	try {

	  def target = getPipelineConfig().compute_coverage ? 'cobertura' : 'test'

	  sh """
	  chmod +x gradlew
	  ./gradlew ${target}
	  """
	} finally {
	  junit keepLongStdio: true, testResults: '**/build/test-results/test/TEST-*.xml'
	  stashCoverageResultGradle key: 'linux'
	}
  }
  
  stage('commit.tests.unit.coverage') {
	publishCoberturaReport(['linux'])
    verifyCoverage key: 'linux'
  }

}

def stashCoverageResultGradle(Map parameters=[:]) {
  String key = parameters.key
  stash_name = "coverage-${key}"
  dir('build/reports/cobertura') {
    saferStash name: stash_name, includes: "coverage.xml"
  }
}
