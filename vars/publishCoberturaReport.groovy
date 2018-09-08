def call() {
    // Publish the Cobertura test coverage report
    step([$class: 'CoberturaPublisher', coberturaReportFile: '**/coverage.xml'])
    archiveArtifacts artifacts: '**/coverage.xml', fingerprint: true
}
