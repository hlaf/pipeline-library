package com.emt.steps

abstract class BaseStep implements Serializable {

	protected Object _steps;
	
	private BaseStep() {}

	BaseStep(Object executor) {
		_steps = executor
	}

}
