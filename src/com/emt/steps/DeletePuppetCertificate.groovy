package com.emt.steps

@groovy.transform.InheritConstructors
class DeletePuppetCertificate extends BaseStep {
	@Override
	Object execute(Map params=[:]) {
		
		String certificate_name = params.certificate_name
		String manager_node = params.get('manager_node', 'puppet_management_node')
		String master = params.get('master', 'puppet')
		String environment = params.get('environment', 'production')
				 
        _steps.node(manager_node) {
			
			_steps.sh """
			curl -ss --insecure --cert /var/lib/puppet/ssl/certs/\${HOSTNAME} \
			     --key /var/lib/puppet/ssl/private_keys/\${HOSTNAME} \
			     -d '{\"desired_state\":\"revoked\"}' -H 'Content-Type: text/pson' \
			     -X PUT https://${master}:8140/${environment}/certificate_status/${certificate_name} && \
			curl -ss --insecure --cert /var/lib/puppet/ssl/certs/\${HOSTNAME} \
			     --key /var/lib/puppet/ssl/private_keys/\${HOSTNAME} \
			     -X DELETE https://${master}:8140/${environment}/certificate_status/${certificate_name}
			"""
		}
	}
}
