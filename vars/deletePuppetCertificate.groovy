def call(String certificate_name,
	     String manager_node='puppet_management_node',
		 String master='puppet',
		 String environment='production') {

  node(manager_node) {

    sh """
    curl -ss --insecure --cert /var/lib/puppet/ssl/certs/\${HOSTNAME} \
         --key /var/lib/puppet/ssl/private_keys/\${HOSTNAME} \
         -d '{\"desired_state\":\"revoked\"}' -H 'Content-Type: text/pson' \
         -X PUT https://${master}:8140/${environment}/certificate_status/${certificate_name} && \
    curl -ss --insecure --cert /var/lib/puppet/ssl/certs/\${HOSTNAME} \
         --key /var/lib/puppet/ssl/private_keys/\${HOSTNAME} \
         -X DELETE https://${master}:8140/${environment}/certificate_status/${certificate_name}
    """
  }

}
