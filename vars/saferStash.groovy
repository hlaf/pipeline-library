import com.emt.steps.CoverageIgnore

@CoverageIgnore
def call(Map parameters=[:]) {
  if (stashExists(parameters.name)) {
    error "The stash '${parameters.name}' already exists"
  }
  stash name: parameters.name, includes: parameters.includes
}
