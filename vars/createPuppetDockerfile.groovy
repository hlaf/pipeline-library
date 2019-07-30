def call(String image_name,
	     String environment='production',
		 String master='puppet') {

    // Create the Dockerfile
    sh """docker run \
             --volumes-from $DOCKER_CONTAINER_ID \
             --entrypoint="" \
             puppet/puppet-agent-alpine /bin/sh -c "
        cd ${env.WORKSPACE}
        apk add --no-cache git
        gem install librarian-puppet --no-ri --no-rdoc
        cat <<EOF > Puppetfile
mod 'nwolfe-image_build',    :git => 'https://github.com/hlaf/puppetlabs-image_build.git',
                             :ref => 'v0.10.0_ruby18_compat-5'
EOF
        librarian-puppet install
        puppet docker dockerfile --master ${master} \
                                 --image-name puppet/${image_name} \
                                 --skip-puppet-install \
                                 --modulepath modules \
                                 --from hlaf/puppet \
                                 --os centos \
                                 --no-inventory \
                                 --no-timestamp \
                                 --trace \
                                 --puppet-env ${environment} > Dockerfile
        "
    """

}
