package myapplication.android.mindall.common.delegateItems.goalItem

import myapplication.android.mindall.common.delegate.DelegateItem

class GoalDelegateItem(
    val value: GoalModel
) : DelegateItem {
    override fun content() = value

    override fun id() = value.hashCode()
}