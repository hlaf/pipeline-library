import com.emt.steps.SetPackageVersion

def call(Map parameters=[:]) {
	new SetPackageVersion(this).execute(parameters)
}
