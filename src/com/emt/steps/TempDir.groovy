package com.emt.steps

@groovy.transform.InheritConstructors
class TempDir extends BaseStep {
	Object execute(Map params=[:]) {
		Closure body = params.body
		def result
		_steps.dir(_steps.pwd(tmp: true)) {
			result = body()
			_steps.deleteDir()
		}
		return result
	}
}
