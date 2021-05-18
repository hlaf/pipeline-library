package com.emt.steps

@groovy.transform.InheritConstructors
class IsBuildStartedByTimer extends BaseStep {
    def required_parameters = []
    Object execute(Map parameters=[:]) {
        validateParameters(parameters)
        def res = _steps.currentBuild.getBuildCauses('hudson.triggers.TimerTrigger$TimerTriggerCause')
        return res.size() > 0
    }
}
