package com.emt.steps

import org.jenkinsci.plugins.workflow.cps.CpsScript

import com.emt.IStepExecutor

abstract class BaseStep implements Serializable {

	protected Object _steps;
	
	BaseStep(CpsScript executor) {
		_steps = executor
	}
	
	BaseStep(IStepExecutor executor) {
		_steps = executor
	}

}
