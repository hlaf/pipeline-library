package com.emt.steps

@groovy.transform.InheritConstructors
class DeletePuppetCertificate extends BaseStep {
	@Override
	Object execute(Map params=[:]) {
		
		String certificate_name = params.certificate_name
		String manager_node = params.get('manager_node', 'puppet_management_node')
		String master = params.get('master', 'puppet')
		String environment = params.get('environment', 'production')
        String ssl_cert_path = params.get('ssl_cert_path',
                                          "/var/lib/puppet/ssl/certs/\${HOSTNAME}.pem")
        String ssl_cert_key = params.get('ssl_cert_key',
                                         "/var/lib/puppet/ssl/private_keys/\${HOSTNAME}.pem")

        _steps.node(manager_node) {
			
			_steps.sh """
			curl -sS -f --insecure --cert ${ssl_cert_path} --key ${ssl_cert_key} \
			     -d '{\"desired_state\":\"revoked\"}' -H 'Content-Type: text/pson' \
			     -X PUT https://${master}:8140/${environment}/certificate_status/${certificate_name} && \
			curl -sS -f --insecure --cert ${ssl_cert_path} --key ${ssl_cert_key} \
			     -X DELETE https://${master}:8140/${environment}/certificate_status/${certificate_name}
			"""
		}
	}
}
