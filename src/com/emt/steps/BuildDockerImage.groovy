package com.emt.steps

import com.emt.common.MapUtils

@groovy.transform.InheritConstructors
class BuildDockerImage extends BaseStep {
	Object execute(Map parameters) {
		Map config = MapUtils.merge(_steps.getPipelineConfig(), parameters)
			
		String image_name = config.image_name
		String image_user = config.image_user ?: 'root'
		String manager_node = config.manager_node ?: 'puppet_management_node'
		String environment = config.environment ?: 'dockerbuilder'
		String master = config.master ?: config.puppet_master ?: 'puppet'
		String from_image_name = config.from_image_name ?: 'hlaf/puppet'
		boolean force = config.getOrDefault('force', false)
			 
		String domain_name = _steps.getDnsDomainName()
	    String master_fqdn = master + '.' + domain_name
		
		_steps.node('docker-slave') {
			if (!force && _steps.dockerImageExists(image_name)) {
				_steps.echo "The image ${image_name} already exists. Skipping build."
				return _steps.docker.image(image_name)
			}

			_steps.echo "Building image ${image_name}"
			
			String image_fqdn = image_name + '.dockerbuilder.' + domain_name
		
			_steps.createPuppetDockerfile(image_name: image_name,
								          image_user: image_user,
										  master: master_fqdn,
								          environment: environment,
								          from_image_name: from_image_name)
			_steps.deletePuppetCertificate(certificate_name: image_fqdn,
				                           manager_node: manager_node,
										   master: master_fqdn,
										   environment: environment)
			def image = _steps.docker.build(image_name, "--no-cache .")
	
			return image
		}
        
	}
}
