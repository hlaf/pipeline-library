import com.emt.common.ChangeSetUtils

Collection<String> getChangeLog(Object step) {
    return new ChangeSetUtils(this).getChangeLog(step)
}
