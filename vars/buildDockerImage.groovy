import com.emt.steps.BuildDockerImage
import com.emt.steps.CoverageIgnore

@CoverageIgnore
def call(Map parameters=[:]) {
	return new BuildDockerImage(this).execute(parameters)
}
