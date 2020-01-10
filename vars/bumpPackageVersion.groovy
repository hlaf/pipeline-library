import com.emt.steps.BumpPackageVersion
import com.emt.steps.CoverageIgnore

@CoverageIgnore
def call(String git_repo_creds,
         String author,
         String author_email,
		 String version_file = "src/${env.JOB_NAME}/__init__.py") {
    
	new BumpPackageVersion().execute(git_repo_creds: git_repo_creds,
								     author: author,
									 author_email: author_email,
									 version_file: version_file)
}
