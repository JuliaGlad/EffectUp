package myapplication.android.mindall.common.delegateItems.daySchedule

import myapplication.android.mindall.common.delegate.DelegateItem

class DayScheduleDelegateItem(
    val value: DayScheduleModel
) : DelegateItem {
    override fun content(): Any = value
    override fun id(): Any = value.hashCode()
}