package com.emt.steps

@groovy.transform.InheritConstructors
class BuildDockerImage extends BaseStep {
	Object execute(Map parameters=[:]) {
		
		String image_name = parameters.image_name
		String image_user = parameters.image_user ?: 'root'
		String manager_node = parameters.manager_node ?: 'puppet_management_node'
		String environment = parameters.environment ?: 'dockerbuilder'
		String from_image_name = parameters.from_image_name ?: 'hlaf/puppet'
		boolean force = parameters.force ?: false
			 
		String domain_name = _steps.getDnsDomainName()
	
		_steps.node('docker-slave') {
			if (!force && _steps.dockerImageExists(parameters.image_name)) {
				_steps.echo "The image ${image_name} already exists. Skipping build."
				return _steps.docker.image(image_name)
			}

			_steps.echo "Building image ${image_name}"
			
			String image_fqdn = image_name + '.dockerbuilder.' + domain_name
		
			_steps.createPuppetDockerfile(image_name: image_name,
								          image_user: image_user,
								          environment: environment,
								          from_image_name: from_image_name)
			_steps.deletePuppetCertificate(image_fqdn, manager_node)
			def image = _steps.docker.build(image_name, "--no-cache .")
	
			return image
		}
        
	}
}
