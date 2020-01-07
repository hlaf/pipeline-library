import com.emt.steps.DeletePuppetCertificate

def call(String certificate_name,
	     String manager_node='puppet_management_node',
		 String master='puppet',
		 String environment='production') {
  new DeletePuppetCertificate(this).execute(certificate_name,
	  								 		manager_node,
											master,
											environment)
}
