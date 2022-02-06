import com.emt.steps.SaferUnstash

def call(Map parameters=[:]) {
  return new SaferUnstash(this).execute(parameters)
}
