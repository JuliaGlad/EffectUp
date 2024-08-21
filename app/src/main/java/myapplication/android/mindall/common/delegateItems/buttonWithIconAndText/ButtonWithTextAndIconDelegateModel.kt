package myapplication.android.mindall.common.delegateItems.buttonWithIconAndText

import myapplication.android.mindall.common.listeners.ButtonClickListener

data class ButtonWithTextAndIconDelegateModel(
    val id: Int,
    val title: String,
    val subtitle: String,
    val icon: Int,
    val listener: ButtonClickListener
)