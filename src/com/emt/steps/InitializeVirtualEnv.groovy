package com.emt.steps

@groovy.transform.InheritConstructors
class InitializeVirtualEnv extends BaseStep {
	Object execute(Map params=[:]) {
		_steps.sh '''
		    label="$(uname)"
		    echo "The label is ${label}"
		
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
	}
}
