package myapplication.android.mindall.common.delegateItems.checkBox

import myapplication.android.mindall.common.delegate.AdapterDelegate
import myapplication.android.mindall.common.delegate.DelegateItem

class CheckBoxDelegateItem(
    val value: CheckBoxModel
)  : DelegateItem {
    override fun content(): Any = value

    override fun id(): Any = value.hashCode()
}