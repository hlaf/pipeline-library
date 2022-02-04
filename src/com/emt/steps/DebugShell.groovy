package com.emt.steps

import groovy.transform.InheritConstructors

@groovy.transform.InheritConstructors
class DebugShell extends BaseStep {

    def required_parameters = []

    Object execute(Map parameters=[:]) {
        validateParameters(parameters);
        
        String cmd = ''
        final String title = '[Debug Shell]'
        while (true) {
            cmd = _steps.input(
                message: 'Jenkins Interactive Debug Shell:',
                parameters: [_steps.string(
                    defaultValue: '',
                    description: "Enter a command or type 'continue' to resume job",
                    name: 'cmd')
                ],
                ok: 'Execute')
            if (cmd == 'continue') {
                break;
            } else {
                try {
                    def ret = Eval.x(_steps, cmd)
                    _steps.echo "${title} Return Value: $ret"
                } catch (e) {
                    _steps.echo "${title} Error: $e"
                }
            }
        }
        _steps.echo "${title} Resuming job...";
    }

}
