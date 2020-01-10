


def call(String build_status,
         String recipient_email) {

  emailext (
	 to: recipient_email,
	 subject: "${build_status}: ${env.JOB_NAME} - Build # ${env.BUILD_NUMBER}",
	 body: """${env.JOB_NAME} - Build # ${env.BUILD_NUMBER}

Check console output at ${env.BUILD_URL} to view the results.""",
  )

}
