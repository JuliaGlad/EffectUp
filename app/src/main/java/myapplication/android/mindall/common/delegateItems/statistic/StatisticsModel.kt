package myapplication.android.mindall.common.delegateItems.statistic

import myapplication.android.mindall.common.listeners.ButtonClickListener

data class StatisticsModel(
    val id: Int,
    val listener: ButtonClickListener
)