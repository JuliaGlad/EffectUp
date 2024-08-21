package myapplication.android.mindall.common.recyclerView.checkBox

import myapplication.android.mindall.common.listeners.ButtonBundleClickListener
import myapplication.android.mindall.common.listeners.ButtonClickListener

data class CheckBoxModel(
    val id: Int?,
    val taskId: String?,
    val title: String?,
    val flag: String?,
    val isComplete: Boolean?,
    val editListener: ButtonClickListener,
    val listener: ButtonBundleClickListener
){
    fun compareTo(item:CheckBoxModel) : Boolean = this.hashCode() == item.hashCode()
}