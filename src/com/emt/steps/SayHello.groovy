package com.emt.steps

import com.emt.IStepExecutor
import com.emt.ioc.ContextRegistry

class SayHello extends BaseStep {
	void execute(String name='human') {
		_steps.echo "Hello, ${name}."
	}
}
