def call(Map parameters=[:]) {
  String key = parameters.key
  String unstash_path = parameters.to
  
  String unstash_metadata_file = "${unstash_path}/UNSTASH.meta"
  
  if (fileExists(unstash_path)) {
	if (fileExists(unstash_metadata_file)) {
		String build_id = readFile unstash_metadata_file
		if (build_id == env.BUILD_NUMBER) {
			return
		} else {
			error "Unstashed data already exists at '${unstash_path}'"
		}
	}
    error "Unstash location already exists: '${unstash_path}'"
  }
  
  dir(unstash_path) { unstash key }
  writeFile file: unstash_metadata_file, text: env.BUILD_NUMBER
  return
}
