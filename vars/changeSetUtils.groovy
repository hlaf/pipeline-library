import com.emt.common.ChangeSetUtils

Collection<String> getChangeLog() {
    return new ChangeSetUtils(this).getChangeLog()
}
