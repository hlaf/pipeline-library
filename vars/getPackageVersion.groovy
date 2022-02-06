import com.emt.steps.GetPackageVersion

def call(Map parameters=[:]) {
	return new GetPackageVersion(this).execute(parameters)
}
