package com.emt.steps

@groovy.transform.InheritConstructors
class InitializeVirtualEnv extends BaseStep {
	Object execute(Map params=[:]) {
		
		boolean load_python = !params.containsKey('load_python') || params.load_python
	
		if (_steps.fileExists('master_venv')) {
			return;
		}

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
