def call(String image_name,
		 String manager_node,
		 String environment='dockerbuilder') {
		 
	node('docker-slave') {
		if (dockerImageExists(image_name)) {
			echo "The image ${image_name} already exists. Skipping build."
			return docker.image(image_name)
		}

		echo "Building image ${image_name}"
		
		domain_name = sh(script: 'dnsdomainname', returnStdout: true).trim()
		image_fqdn = image_name + '.dockerbuilder.' + domain_name
	
		createPuppetDockerfile(image_name, environment)
		deletePuppetCertificate(image_fqdn, manager_node)
		image = docker.build(image_name)

		return image
	}
}
