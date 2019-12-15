def call(Map parameters=[:]) {

	String image_name = parameters.image_name
	String manager_node = parameters.manager_node
	String environment = parameters.environment ?: 'dockerbuilder'
	boolean force = parameters.force ?: false
		 
	node ('linux') {	 
		domain_name = sh(script: 'dnsdomainname', returnStdout: true).trim()
		if (!domain_name) {
			error "Could not determine the DNS domain name"
		}
	}

	node('docker-slave') {
		if (!force && dockerImageExists(parameters.image_name)) {
			echo "The image ${image_name} already exists. Skipping build."
			return docker.image(image_name)
		}

		echo "Building image ${image_name}"
		
		image_fqdn = image_name + '.dockerbuilder.' + domain_name
	
		createPuppetDockerfile(image_name: image_name,
			                   environment: environment)
		deletePuppetCertificate(image_fqdn, manager_node)
		image = docker.build(image_name, "--no-cache .")

		return image
	}
}
