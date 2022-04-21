import com.emt.steps.CheckoutFromGit

def call(String git_repo_creds,
         String repo_url,
		 String branch='master',
         String target_dir=null) {
  new CheckoutFromGit(this).execute(repo_creds: git_repo_creds,
	  								repo_url: repo_url,
									branch: branch,
                                    target_dir: target_dir)
}
