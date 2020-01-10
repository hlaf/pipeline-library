import com.emt.steps.IsBuildStartedByTimer

def call() {
  return new IsBuildStartedByTimer(this).execute()
}
