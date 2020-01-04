import com.emt.build.MsBuild
import com.emt.ioc.ContextRegistry

/**
 * Example custom step for easy use of MsBuild inside Jenkinsfiles
 * @param solutionPath Path to .sln file
 * @return
 */
def call(String solutionPath) {
    ContextRegistry.registerDefaultContext(this)

    def msbuilder = new MsBuild(solutionPath)
    msbuilder.build()
}