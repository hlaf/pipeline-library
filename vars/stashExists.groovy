import com.emt.steps.StashExists

def call(String name) {
	return new StashExists(this).execute(name: name);
}
