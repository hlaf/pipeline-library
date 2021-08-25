import com.emt.steps.RunParallel

def call(Map parameters=[:]) {
	new RunParallel(this).execute(parameters)
}
