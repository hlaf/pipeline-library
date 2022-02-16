package com.emt.steps

@groovy.transform.InheritConstructors
class StrictSh extends BaseStep {
    def required_parameters = ['script']

    Object execute(Map parameters=[:]) {
        validateParameters(parameters)
        String script = parameters.script
        def res = _steps.sh(script: """
          set -e
          set -o pipefail
          ${script}
        """)
        return res
    }
}
