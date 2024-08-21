package myapplication.android.mindall.common.delegateItems.floatingActionButton

import myapplication.android.mindall.common.delegate.DelegateItem

class FabDelegateItem(
    val value: FabModel
) : DelegateItem {

    override fun content(): Any = value

    override fun id(): Any = value.hashCode()
}