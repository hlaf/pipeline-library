package com.emt

/**
 * Interface for calling any necessary Jenkins steps. This will be mocked in unit tests.
 */
abstract class IStepExecutor {
    abstract int sh(String command)
	abstract String sh(Map params)
    abstract void echo(String message)
    abstract void error(String message)
	Object node(String name, Closure body) { return body() }
}
