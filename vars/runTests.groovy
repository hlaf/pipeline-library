import com.emt.steps.RunTests

def call(Map parameters=[:]) {
	new RunTests(this).execute(parameters)
}
