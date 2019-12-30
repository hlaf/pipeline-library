def call(String name) {
  
  try {
	tempDir {
      unstash name: name
	}
  } catch (hudson.AbortException e) {
    return false;
  }

  return true;
}
