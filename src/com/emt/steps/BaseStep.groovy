package com.emt.steps

import com.emt.IStepExecutor
import com.emt.ioc.ContextRegistry

abstract class BaseStep implements Serializable {

	protected Object _steps;
	
	BaseStep(Object executor) {
		_steps = executor
	}

}
