import com.emt.steps.GetPipelineConfig

def call() {
	return new GetPipelineConfig(this).execute()
}
