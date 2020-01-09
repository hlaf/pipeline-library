package com.emt.steps

@groovy.transform.InheritConstructors
class IsBuildReplayed extends BaseStep {
	boolean execute() {
		 def res = _steps.currentBuild.getBuildCauses('org.jenkinsci.plugins.workflow.cps.replay.ReplayCause')
		 return res.size() > 0
	}
}
