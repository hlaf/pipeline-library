package com.emt.steps

import com.emt.IStepExecutor
import com.emt.ioc.ContextRegistry

abstract class BaseStep implements Serializable {

	protected IStepExecutor _steps;
	
	BaseStep() {
		_steps = ContextRegistry.getContext().getStepExecutor()
	}
	
}
