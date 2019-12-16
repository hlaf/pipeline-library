def call() {
  
  node ('linux') {
    domain_name = sh(script: 'dnsdomainname', returnStdout: true).trim()
    if (!domain_name) {
      error "Could not determine the DNS domain name"
	}
  }
  
  return domain_name
}
