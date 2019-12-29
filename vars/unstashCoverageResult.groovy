def call(Map parameters=[:]) {
  String key = parameters.key
  
  String unstash_path = "./coverage_results/${key}"
  dir(unstash_path) { unstash "coverage-${key}" }
  return unstash_path
}
