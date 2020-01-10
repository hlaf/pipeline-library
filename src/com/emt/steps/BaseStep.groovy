package com.emt.steps

abstract class BaseStep implements Serializable {

	protected Object _steps;
	
	BaseStep(Object executor) {
		_steps = executor
	}

	abstract Object execute(Map args=[:]);

}
