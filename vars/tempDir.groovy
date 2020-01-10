


def call(closure) {
  dir(pwd(tmp: true)) {
	  closure()
	  deleteDir()
  }
}
