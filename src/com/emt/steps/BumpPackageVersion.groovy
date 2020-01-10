package com.emt.steps

@groovy.transform.InheritConstructors
class BumpPackageVersion extends BaseStep {
	Object execute(Map parameters=[:]) {
		
		String git_repo_creds = parameters.git_repo_creds
		String author = parameters.author
		String author_email = parameters.author_email
		String version_file = parameters.get('version_file', "src/${_steps.env.JOB_NAME}/__init__.py")
   
	   def tag_name = 'latest'
	 
	   // TODO: Replace the hyphens with underscores in env.JOB_NAME when building
	   // the default version_file path
	
	   _steps.initializeVirtualEnv()
	
	   _steps.sshagent([git_repo_creds]) {
		   _steps.sh """
	        echo "The version file is ${version_file}"
	        current_version=\$(grep __version__ $version_file | awk -F\\   '{print \$3 }' | sed "s/'//g")
	        source master_venv/bin/activate
	        \\pip install semver --upgrade
	        new_version=\$(python -c "import semver; print semver.bump_patch(\'\${current_version}\')")
	    
	        # Update the __init__.py file with the new version.
	        sed -i s/\$current_version/\$new_version/ $version_file
	        
			git config --global user.name $author
			git config --global user.email $author_email
			
	        git commit -m "Bump version to \${new_version}." $version_file
	        git push --set-upstream origin master
	        
	        git tag -fa \$new_version -m \"Create tag for version \${new_version}.\"
	        git tag -fa \"${tag_name}\" -m \"Update the '${tag_name}' tag.\"
	           
	        # delete tag on remote in order not to fail pushing the new one
	        git push origin :refs/tags/${tag_name}
	       
	        # push the tags
	        git push -f origin refs/tags/${tag_name}:refs/tags/${tag_name}
	        git push -f origin refs/tags/\${new_version}:refs/tags/\${new_version}
	    """
	   }
	}
}
