def call(Map parameters=[:]) {
  String key = parameters.key
  
  stash name: "coverage-${key}", includes: "coverage.xml"
}
