import com.emt.steps.CoverageIgnore
import com.emt.steps.SayHello

@CoverageIgnore
def call(String name = 'human') {
	new SayHello(this).execute(name: name)
}
