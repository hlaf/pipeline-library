


def call(Map parameters=[:]) {
  String key = parameters.key
  String unstash_path = parameters.to
  
  String unstash_metadata_file = "${unstash_path}/UNSTASH.meta"
  
  if (fileExists(unstash_path)) {
	if (fileExists(unstash_metadata_file)) {
		
		def props = readJSON file: unstash_metadata_file
		if (props.stash_name != key) {
          error "Location '${unstash_path}' contains data for stash '${props.stash_name}'"
		} else if (props.build_number == env.BUILD_NUMBER) {
		  return
		} else {
		  error "Location '${unstash_path}' contains data from a prior build"
		}
	}
    error "Unstash location already exists: '${unstash_path}'"
  }
  
  dir(unstash_path) { unstash key }
  def data = readJSON text: '{}'
  data.build_number = env.BUILD_NUMBER
  data.stash_name = key
  writeJSON file: unstash_metadata_file, json: data
  
  return
}
