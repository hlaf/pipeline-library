package com.emt.steps

@groovy.transform.InheritConstructors
class IsBuildStartedByTimer extends BaseStep {
	boolean execute() {
		def res = _steps.currentBuild.getBuildCauses('hudson.triggers.TimerTrigger.TimerTriggerCause')
	}
}
