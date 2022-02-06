
import com.emt.steps.InitializeVirtualEnv


def call(Map parameters=[:]) {
	new InitializeVirtualEnv(this).execute(parameters);
}
