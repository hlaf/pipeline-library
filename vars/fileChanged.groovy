import com.emt.steps.FileChanged

def call(String name) {
  return new FileChanged(this).execute(name: name)
}
