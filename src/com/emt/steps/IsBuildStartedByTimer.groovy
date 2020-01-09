package com.emt.steps

@groovy.transform.InheritConstructors
class IsBuildStartedByTimer extends BaseStep {
	boolean execute() {
		 def build_causes = _steps.currentBuild.getBuildCauses()
		 return build_causes.contains('hudson.triggers.TimerTrigger.TimerTriggerCause')
	}
}
