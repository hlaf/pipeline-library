import com.emt.steps.TempDir

def call(Closure body) {
  return new TempDir(this).execute(body: body)
}
