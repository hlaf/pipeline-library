package com.emt.steps

import org.somecompany.IStepExecutor
import org.somecompany.ioc.ContextRegistry

class SayHello implements Serializable {

	SayHello() {}

	void execute(String name='human') {
		IStepExecutor steps = ContextRegistry.getContext().getStepExecutor()

		steps.echo name
		
	}
}
