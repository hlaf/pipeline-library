import com.emt.steps.CoverageIgnore
import com.emt.steps.GetDnsDomainName

@CoverageIgnore
def call() {
  return new GetDnsDomainName(this).execute()
}
