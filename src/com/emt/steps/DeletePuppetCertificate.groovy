package com.emt.steps

@groovy.transform.InheritConstructors
class DeletePuppetCertificate extends BaseStep {
	@Override
	Object execute(Map params=[:]) {
        Map config = MapUtils.merge(_steps.getPipelineConfig(), params)
		
		String certificate_name = config.certificate_name
		String manager_node = config.get('manager_node', 'puppet_management_node')
		String master = config.get('master', 'puppet')
		String environment = config.get('environment', 'production')
        String ssl_cert_path = config.get('ssl_cert_path',
                                          "/var/lib/puppet/ssl/certs/\${HOSTNAME}.pem")
        String ssl_cert_key = config.get('ssl_cert_key',
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
