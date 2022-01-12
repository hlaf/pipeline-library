package com.emt.steps

import com.emt.common.CertUtils
import com.emt.common.MapUtils

@groovy.transform.InheritConstructors
class DeletePuppetCertificate extends BaseStep {

    def required_parameters = ['certificate_name']

	@Override
	Object execute(Map parameters=[:]) {
        Map config = MapUtils.merge(_steps.getPipelineConfig(), parameters)
        validateParameters(config)

		String certificate_name = config.certificate_name
		String manager_node = config.get('manager_node', 'puppet_management_node')
		String master = config.get('master', 'puppet')
		String environment = config.get('environment', 'production')
        String ssl_cert_path = config.get('ssl_cert_path',
                                          "/var/lib/puppet/ssl/certs/\${HOSTNAME}.pem")
        String ssl_cert_key = config.get('ssl_cert_key',
                                         "/var/lib/puppet/ssl/private_keys/\${HOSTNAME}.pem")
        String puppetapi_cert_name = config.get('puppetapi_cert_name')

        if (puppetapi_cert_name != null) {
          ssl_cert_path = CertUtils.getSslCertPath(puppetapi_cert_name)
          ssl_cert_key = CertUtils.getSslKeyPath(puppetapi_cert_name)
        }

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
