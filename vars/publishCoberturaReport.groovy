import com.emt.steps.PublishCoberturaReport

def call(results=[]) {
    new PublishCoberturaReport(this).execute(results: results)
}
