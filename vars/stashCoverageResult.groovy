def call(Map parameters=[:]) {
  String key = parameters.key
  
  stash_name = "coverage-${key}"
  
  if (stashExists(stash_name)) {
    error "The stash '${stash_name}' already exists"
  }

  stash name: stash_name, includes: "coverage.xml"
}
