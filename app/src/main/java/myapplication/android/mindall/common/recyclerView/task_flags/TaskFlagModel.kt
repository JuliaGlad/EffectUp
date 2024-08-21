package myapplication.android.mindall.common.recyclerView.task_flags

data class TaskFlagModel(
    val id: Int,
    val flag: Int,
    val tasksCount: String?,
    val color: Int
) {
    fun compareTo(other: TaskFlagModel) : Boolean{
        return this.hashCode() == other.hashCode()
    }
}