package com.emt

class StepExecutor implements IStepExecutor {
    private _steps

    StepExecutor(steps) {
        this._steps = steps
    }

    @Override
    int sh(String command) {
        this._steps.sh returnStatus: true, script: "${command}"
    }

    @Override
    void error(String message) {
        this._steps.error(message)
    }

	@Override
	public void echo(String message) {
		this._steps.echo(message)
	}
}
