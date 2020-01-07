import com.emt.steps.GetDnsDomainName

def call() {
  return new GetDnsDomainName(this).execute()
}
