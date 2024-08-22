package myapplication.android.mindall.common.delegateItems.habitTrackerDaily

import myapplication.android.mindall.common.delegate.DelegateItem

class HabitTrackerDelegateItem(
    val value: HabitTrackerModel
) : DelegateItem {
    override fun content() = value

    override fun id() = value.hashCode()
}