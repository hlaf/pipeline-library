package com.emt.steps

@groovy.transform.InheritConstructors
class GetDnsDomainName extends BaseStep {
	String execute() {
		def domain_name
		_steps.node ('linux') {
			domain_name = _steps.sh(script: 'dnsdomainname', returnStdout: true).trim()
			if (!domain_name) {
			  _steps.error "Could not determine the DNS domain name"
			}
		}

		return domain_name
	}
}
