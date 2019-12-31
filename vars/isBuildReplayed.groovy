def call(Map parameters=[:]) {
  def res = currentBuild.getBuildCauses('org.jenkinsci.plugins.workflow.cps.replay.ReplayCause')
  return res.size() > 0	
}
