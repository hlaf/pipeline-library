package com.emt.steps

import com.emt.common.MapUtils

@groovy.transform.InheritConstructors
class InitializeVirtualEnv extends BaseStep {

    private final static String venv_name = 'master_venv'

    def required_parameters = []

    Object execute(Map parameters=[:]) {
        Map config = MapUtils.merge(_steps.getPipelineConfig(), parameters)
        validateParameters(config)

        if (_steps.fileExists(venv_name)) {
            return;
        }

        def setup_script = ""
        if (_steps.sh(script: 'module list', returnStatus: true) == 0) {
            setup_script += """
                label="\$(uname)"
                case "\${label}" in
                  Darwin* )
                    module load python
                    ;;
                  Linux* )
                    module load python/2.7-linux-x64-centos-rpm
                    ;;
                esac
            """
        }

        setup_script += """
		        virtualenv ${venv_name}
		"""

        _steps.sh(setup_script)
    }
}
