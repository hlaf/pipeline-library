import com.emt.steps.SaferStash

def call(Map parameters=[:]) {
  new SaferStash(this).execute(parameters)
}
