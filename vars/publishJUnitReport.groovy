import com.emt.steps.CoverageIgnore

@CoverageIgnore
def call() {
    junit keepLongStdio: true, testResults: '**/test_results.xml'
}
