package com.emt.steps

@groovy.transform.InheritConstructors
class SayHello extends BaseStep {
	void execute(String name='human') {
		_steps.echo "Hello, ${name}."
	}
}
