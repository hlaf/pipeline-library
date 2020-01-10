import com.emt.steps.CoverageIgnore

@CoverageIgnore
def call() {

	return PipelineConfigManager.inst().get("${env.JOB_NAME}@${env.BUILD_NUMBER}")
  
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
