import com.emt.steps.UnstashCoverageResult

def call(Map parameters=[:]) {
  return new UnstashCoverageResult(this).execute(parameters)
}
