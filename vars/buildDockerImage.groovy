import com.emt.steps.BuildDockerImage



def call(Map parameters=[:]) {
	return new BuildDockerImage(this).execute(parameters)
}
