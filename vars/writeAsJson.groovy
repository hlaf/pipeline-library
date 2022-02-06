import com.emt.steps.WriteAsJson

def call(Map parameters=[:]) {
  return new WriteAsJson(this).execute(parameters)
}
