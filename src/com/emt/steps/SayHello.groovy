package com.emt.steps

@groovy.transform.InheritConstructors
class SayHello extends BaseStep {
	Object execute(Map params=[:]) {
		String name = params.name ?: 'human'
		_steps.echo "Hello, ${name}."
	}
}
