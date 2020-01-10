import com.emt.steps.CoverageIgnore
import com.emt.steps.InitializeVirtualEnv

@CoverageIgnore
def call() {
	new InitializeVirtualEnv(this).execute();
}
