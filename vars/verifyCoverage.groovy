import com.cloudbees.groovy.cps.NonCPS
import com.emt.steps.VerifyCoverage

def call(Map parameters=[:]) {
    new VerifyCoverage(this).execute(parameters)
}
