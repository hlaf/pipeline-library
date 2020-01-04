import com.emt.steps.SayHello

def call(String name = 'human') {
	new SayHello().execute(name)
}
