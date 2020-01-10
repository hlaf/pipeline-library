import com.emt.steps.CoverageIgnore

@CoverageIgnore
def call(Map parameters=[:]) {
  String key = parameters.key
  
  String unstash_path = "./coverage_results/${key}"  
  saferUnstash key: "coverage-${key}", to: unstash_path
  
  return unstash_path
}
