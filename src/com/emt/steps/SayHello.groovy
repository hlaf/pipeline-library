package com.emt.steps

import com.emt.IStepExecutor
import com.emt.ioc.ContextRegistry

class SayHello implements Serializable {

	SayHello() {}

	void execute(String name='human') {
		IStepExecutor steps = ContextRegistry.getContext().getStepExecutor()

		steps.echo name
		
	}
}
