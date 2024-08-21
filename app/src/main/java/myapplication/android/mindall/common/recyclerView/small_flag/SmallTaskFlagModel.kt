package myapplication.android.mindall.common.recyclerView.small_flag

data class SmallTaskFlagModel(
    val id: Int,
    val flag: Int,
    val tasksCount: String?,
    val color: Int
) {
    fun compareTo(other: SmallTaskFlagModel) : Boolean{
        return this.hashCode() == other.hashCode()
    }
}