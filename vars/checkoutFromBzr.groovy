import com.emt.steps.CheckoutFromBzr

def call(Map parameters=[:]) {
  new CheckoutFromBzr(this).execute(parameters)
}
