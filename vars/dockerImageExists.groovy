import com.emt.steps.DockerImageExists

def call(String image_name) {
  return new DockerImageExists(this).execute(image_name: image_name)
}
