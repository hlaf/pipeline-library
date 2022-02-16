import com.emt.steps.StrictSh

def call(String script) {
	return new StrictSh(this).execute(script: script)
}
