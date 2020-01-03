package org.somecompany

/**
 * Interface for calling any necessary Jenkins steps. This will be mocked in unit tests.
 */
interface IStepExecutor {
    int sh(String command)
    void echo(String message)
    void error(String message)
}
