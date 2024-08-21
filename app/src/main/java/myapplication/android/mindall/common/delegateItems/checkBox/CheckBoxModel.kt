package myapplication.android.mindall.common.delegateItems.checkBox

import myapplication.android.mindall.common.listeners.ButtonBundleClickListener
import myapplication.android.mindall.common.listeners.ButtonClickListener
import myapplication.android.mindall.common.listeners.ButtonStringClickListener

data class CheckBoxModel(
    val id: Int?,
    val taskId: String?,
    val title: String?,
    val flag: String?,
    val isComplete: Boolean?,
    val editListener: ButtonClickListener,
    val listener: ButtonBundleClickListener
)