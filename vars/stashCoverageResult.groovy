import com.emt.steps.StashCoverageResult

def call(Map parameters=[:]) {
	new StashCoverageResult(this).execute(parameters)
}
