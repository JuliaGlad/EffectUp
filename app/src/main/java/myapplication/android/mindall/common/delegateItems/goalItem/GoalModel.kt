package myapplication.android.mindall.common.delegateItems.goalItem

data class GoalModel(
    val id: Int,
    val goal: String
) {
    fun compareTo(other: GoalModel) = this.hashCode() == other.hashCode()
}
