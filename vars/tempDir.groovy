import com.emt.steps.CoverageIgnore

@CoverageIgnore
def call(closure) {
  dir(pwd(tmp: true)) {
	  closure()
	  deleteDir()
  }
}
