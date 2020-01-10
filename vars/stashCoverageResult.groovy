


def call(Map parameters=[:]) {
  String key = parameters.key
  stash_name = "coverage-${key}"
  saferStash name: stash_name, includes: "coverage.xml"
}
