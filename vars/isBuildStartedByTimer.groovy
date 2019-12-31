def call(Map parameters=[:]) {
  def res = currentBuild.getBuildCauses('hudson.triggers.TimerTrigger.TimerTriggerCause')
  return res.size() > 0	
}
