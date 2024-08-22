package myapplication.android.mindall.common.delegateItems.habit

import myapplication.android.mindall.common.delegate.DelegateItem

class HabitDelegateItem(
    val value: HabitModel
) : DelegateItem {
    override fun content() = value

    override fun id() = value.hashCode()
}