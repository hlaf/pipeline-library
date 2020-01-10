


def call(String image_name) {
  ret = sh returnStatus:true, script: """
             docker inspect --type=image ${image_name} > /dev/null 2>&1
        """
  return ret == 0
}
