import com.emt.steps.CoverageIgnore
import com.emt.steps.IsBuildReplayed

@CoverageIgnore
def call() {
  return new IsBuildReplayed(this).execute()
}
