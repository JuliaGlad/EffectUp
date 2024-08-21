package myapplication.android.mindall.common.delegateItems.tracker

import myapplication.android.mindall.common.delegate.DelegateItem

class TrackerDelegateItem(
    val value: TrackerModel
) : DelegateItem {
    override fun content(): Any = value
    override fun id(): Any = value.hashCode()
}