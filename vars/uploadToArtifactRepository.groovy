import com.emt.steps.UploadToArtifactRepository

def call(String artifact_repo_url,
	     String artifact_repo_creds) {
    new UploadToArtifactRepository(this).execute(artifact_repo_url: artifact_repo_url,
		                                         artifact_repo_creds: artifact_repo_creds)
}
