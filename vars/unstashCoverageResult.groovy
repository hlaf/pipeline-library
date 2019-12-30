def call(Map parameters=[:]) {
  String key = parameters.key
  
  String unstash_path = "./coverage_results/${key}"
  
  if (fileExists(unstash_path)) {
    error "Unstash location already exists: '${unstash_path}'"
  }
  
  dir(unstash_path) { unstash "coverage-${key}" }
  return unstash_path
}
