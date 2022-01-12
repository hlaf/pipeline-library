package com.emt.steps

@groovy.transform.InheritConstructors
class CreatePuppetDockerfile extends BaseStep {
    Object execute(Map parameters=[:]) {
        def required_parameters = ["image_name"]
        for (p_name in required_parameters) {
            if (!parameters.containsKey(p_name)) {
                throw new Exception(
                        "The parameter '${p_name}' is mandatory");
            }
        }
        String image_name = parameters.image_name
        String image_user = parameters.get('image_user', 'root')
        String environment = parameters.get('environment', 'production')
        String master = parameters.get('master', 'puppet')
        String from_image_name = parameters.get('from_image_name', 'hlaf/puppet')

        // Create the Dockerfile
        _steps.sh """docker run \
             --volumes-from \$DOCKER_CONTAINER_ID \
             --entrypoint="" \
             puppet/puppet-agent-alpine /bin/sh -c "
        cd ${_steps.env.WORKSPACE}
        apk add --no-cache git
        gem install librarian-puppet --no-ri --no-rdoc
        cat <<EOF > Puppetfile
mod 'nwolfe-image_build',    :git => 'https://github.com/hlaf/puppetlabs-image_build.git',
                             :ref => 'v0.10.0_ruby18_compat-6'
EOF
        librarian-puppet install
        puppet docker dockerfile --master ${master} \
                                 --image-name puppet/${image_name} \
                                 --image-user ${image_user} \
                                 --skip-puppet-install \
                                 --modulepath modules \
                                 --from ${from_image_name} \
                                 --os centos \
                                 --no-inventory \
                                 --no-timestamp \
                                 --puppet-extra-settings '--no-stringify_facts' \
                                 --trace \
                                 --puppet-env ${environment} > Dockerfile
        "
		"""

    }
}
