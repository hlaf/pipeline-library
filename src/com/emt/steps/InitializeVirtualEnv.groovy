package com.emt.steps

import com.emt.common.MapUtils

@groovy.transform.InheritConstructors
class InitializeVirtualEnv extends BaseStep {
	Object execute(Map params=[:]) {
	
        Map config = MapUtils.merge(_steps.getPipelineConfig(), params)
		boolean load_python = config.getOrDefault('load_python', true)
		
        if (load_python) {
			_steps.sh '''
	    	    label="$(uname)"
	    	    case "${label}" in
	    	      Darwin* )
	    	        module load python
	    	        ;;
	    	      Linux* )
	    	        module load python/2.7-linux-x64-centos-rpm
	    	        ;;
	    	    esac
	    	
		        virtualenv master_venv
		   '''
		} else {
            _steps.sh '''
		        virtualenv master_venv
		    '''
		}
		
	}
}
