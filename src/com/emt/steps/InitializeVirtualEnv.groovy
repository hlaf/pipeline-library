package com.emt.steps

import com.emt.common.MapUtils

@groovy.transform.InheritConstructors
class InitializeVirtualEnv extends BaseStep {
    
    private final static String venv_name = 'master_venv'
    
    Object execute(Map params=[:]) {

        Map config = MapUtils.merge(_steps.getPipelineConfig(), params)
        boolean load_python = config.getOrDefault('load_python', true)

        if (_steps.fileExists(venv_name)) {
            return;
        }

        if (load_python) {
            _steps.sh """
	    	    label="\$(uname)"
	    	    case "\${label}" in
	    	      Darwin* )
	    	        module load python
	    	        ;;
	    	      Linux* )
	    	        module load python/2.7-linux-x64-centos-rpm
	    	        ;;
	    	    esac
	    	
		        virtualenv ${venv_name}
		   """
        } else {
            _steps.sh """
		        virtualenv ${venv_name}
		    """
        }
    }
}
