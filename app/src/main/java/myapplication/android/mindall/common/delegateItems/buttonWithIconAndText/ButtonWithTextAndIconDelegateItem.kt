package myapplication.android.mindall.common.delegateItems.buttonWithIconAndText

import myapplication.android.mindall.common.delegate.DelegateItem

class ButtonWithTextAndIconDelegateItem(
    val value: ButtonWithTextAndIconDelegateModel
) : DelegateItem {
    override fun content(): Any = value
    override fun id(): Any = value.hashCode()
}