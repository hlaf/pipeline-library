import com.emt.steps.CoverageIgnore
import com.emt.steps.IsBuildStartedByTimer

@CoverageIgnore
def call() {
  return new IsBuildStartedByTimer(this).execute()
}
