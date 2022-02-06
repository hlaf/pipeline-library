import com.emt.steps.NotifyBuild

def call(String build_status,
         String recipient_email) {
  new NotifyBuild(this).execute(build_status: build_status,
	                            recipient_email: recipient_email)
}
