import com.emt.steps.CoverageIgnore
import com.emt.steps.DeletePuppetCertificate

@CoverageIgnore
def call(String certificate_name,
	     String manager_node='puppet_management_node',
		 String master='puppet',
		 String environment='production') {
  new DeletePuppetCertificate(this).execute(certificate_name: certificate_name,
	  								 		manager_node: manager_node,
											master: master,
											environment: environment)
}
