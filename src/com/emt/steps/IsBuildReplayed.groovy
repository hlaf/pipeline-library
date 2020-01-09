package com.emt.steps

@groovy.transform.InheritConstructors
class IsBuildReplayed extends BaseStep {
	boolean execute() {
		 def build_causes = _steps.currentBuild.getBuildCauses()
		 return build_causes.contains('org.jenkinsci.plugins.workflow.cps.replay.ReplayCause')
	}
}
