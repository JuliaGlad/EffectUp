package myapplication.android.mindall.common.delegateItems.adviseBox

import myapplication.android.mindall.common.delegate.DelegateItem

class AdviseBoxDelegateItem(
    val value: AdviseBoxModel
) : DelegateItem {
    override fun content(): Any = value
    override fun id(): Any = value.hashCode()
}