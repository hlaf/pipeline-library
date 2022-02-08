#!groovy

@Library('emt-pipeline-lib@master') _

repo_creds = 'emt-jenkins-github-ssh'
repo_url = 'git@github.com:hlaf/pipeline-library.git'
branch_name = 'master'

getPipelineConfig().compute_coverage = true

node('docker-slave') {

  stage('commit.checkout') {
    checkoutFromGit(repo_creds, repo_url, branch_name)
  }

  stage('commit.tests.unit') {
    try {
      def target = 'test'
      sh """
      chmod +x gradlew
      ./gradlew ${target}
      """
    } finally {
      junit keepLongStdio: true, testResults: '**/build/test-results/test/TEST-*.xml'
    }
  }

  stage('commit.tests.unit.coverage.jacoco') {
    try {
      def target = 'jacocoTestReport'
      sh """
      chmod +x gradlew
      ./gradlew ${target}
      """
    } finally {
      junit keepLongStdio: true, testResults: '**/build/test-results/test/TEST-*.xml'
    }

    publishCoverage adapters: [jacocoAdapter(path: 'build/reports/jacoco/coverage.xml',
                                             thresholds: [[failUnhealthy: true,
                                                          thresholdTarget: 'Line',
                                                          unhealthyThreshold: 95.0,
                                                          unstableThreshold: 95.0],
                                                          [failUnhealthy: true,
                                                          thresholdTarget: 'Conditional',
                                                          unhealthyThreshold: 95.0,
                                                          unstableThreshold: 95.0]
                                             ]
                               )
                    ],
                    failNoReports: true,
                    failUnhealthy: true,
                    failUnstable: true,
                    sourceFileResolver: sourceFiles('STORE_LAST_BUILD')
  }

  stage('commit.tests.unit.coverage.cobertura') {
  
    try {
      def target = 'cobertura'
      sh """
      chmod +x gradlew
      ./gradlew ${target}
      """
    } finally {
      junit keepLongStdio: true, testResults: '**/build/test-results/test/TEST-*.xml'
    }

    publishCoverage adapters: [istanbulCoberturaAdapter(
                                 path: 'build/reports/cobertura/coverage.xml',
                                 thresholds: [[failUnhealthy: true,
                                              thresholdTarget: 'Line',
                                              unhealthyThreshold: 95.0,
                                              unstableThreshold: 95.0],
                                              [failUnhealthy: true,
                                              thresholdTarget: 'Conditional',
                                              unhealthyThreshold: 95.0,
                                              unstableThreshold: 95.0]
                                 ]
                               )
                    ],
                    failNoReports: true,
                    failUnhealthy: true,
                    failUnstable: true,
                    sourceFileResolver: sourceFiles('STORE_LAST_BUILD')
  }

}

def stashCoverageResultGradle(Map parameters=[:]) {
  String key = parameters.key
  stash_name = "coverage-${key}"
  dir('build/reports/jacoco') {
    saferStash name: stash_name, includes: "coverage.xml"
  }
}
