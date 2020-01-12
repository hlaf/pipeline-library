package com.emt.steps

import java.util.Map

@groovy.transform.InheritConstructors
class GetPipelineConfig extends BaseStep {
	Object execute(Map parameters=[:]) {
		String unique_id = "${_steps.env.JOB_NAME}@${_steps.env.BUILD_NUMBER}"
		return PipelineConfigManager.inst().get(unique_id)
	}
}

protected class PipelineConfigManager {
	
	private final Map _items;
	private static PipelineConfigManager _inst;

	private PipelineConfigManager() {
		_items = [:].withDefault { k -> [:] };
	}

	protected synchronized static PipelineConfigManager inst() {
		if (_inst == null) {
			_inst = new PipelineConfigManager();
		}
		return _inst;
	}
	
	protected get(String config_name) {
		return _items.get(config_name)
	}

}

