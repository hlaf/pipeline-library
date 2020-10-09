import java.util.Map

import com.emt.steps.DeletePuppetCertificate

def call(Map parameters=[:]) {
  new DeletePuppetCertificate(this).execute(parameters)
}
