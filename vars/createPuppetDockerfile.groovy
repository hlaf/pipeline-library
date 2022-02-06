import com.emt.steps.CreatePuppetDockerfile

def call(Map parameters=[:]) {
  new CreatePuppetDockerfile(this).execute(parameters)
}
