def call(String image_name,
	     String domain_name,
		 String manager_node) {
		 
	node('docker-slave') {
		if (dockerImageExists(image_name)) {
			echo "The image ${image_name} already exists. Skipping build."
			return
		}
		
		echo "Building image ${image_name}"
		image_fqdn = image_name + '.dockerbuilder.' + domain_name
	
		createPuppetDockerfile(image_name)
		deletePuppetCertificate(image_fqdn, manager_node=manager_node)
		image = docker.build(image_name)

		return image
	}
}
