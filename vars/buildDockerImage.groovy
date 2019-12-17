def call(Map parameters=[:]) {

	String image_name = parameters.image_name
	String image_user = parameters.image_user ?: 'root'
	String manager_node = parameters.manager_node ?: 'puppet_management_node'
	String environment = parameters.environment ?: 'dockerbuilder'
	String from_image_name = parameters.from_image_name ?: 'hlaf/puppet'
	boolean force = parameters.force ?: false
		 
	domain_name = getDnsDomainName()

	node('docker-slave') {
		if (!force && dockerImageExists(parameters.image_name)) {
			echo "The image ${image_name} already exists. Skipping build."
			return docker.image(image_name)
		}

		echo "Building image ${image_name}"
		
		image_fqdn = image_name + '.dockerbuilder.' + domain_name
	
		createPuppetDockerfile(image_name: image_name,
			                   image_user: image_user,
			                   environment: environment,
							   from_image_name: from_image_name)
		deletePuppetCertificate(image_fqdn, manager_node)
		image = docker.build(image_name, "--no-cache .")

		return image
	}
}
