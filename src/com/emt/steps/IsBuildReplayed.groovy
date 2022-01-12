package com.emt.steps

@groovy.transform.InheritConstructors
class IsBuildReplayed extends BaseStep {
    def required_parameters = []
    Object execute(Map parameters=[:]) {
        validateParameters(parameters)
        def res = _steps.currentBuild.getBuildCauses('org.jenkinsci.plugins.workflow.cps.replay.ReplayCause')
        return res.size() > 0
    }
}
