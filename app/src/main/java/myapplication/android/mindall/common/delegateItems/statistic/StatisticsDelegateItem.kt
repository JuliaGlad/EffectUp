package myapplication.android.mindall.common.delegateItems.statistic

import myapplication.android.mindall.common.delegate.DelegateItem

class StatisticsDelegateItem(val value: StatisticsModel) : DelegateItem {

    override fun content() = value

    override fun id() = value.hashCode()
}