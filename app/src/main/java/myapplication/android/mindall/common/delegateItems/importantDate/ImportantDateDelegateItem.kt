package myapplication.android.mindall.common.delegateItems.importantDate

import androidx.recyclerview.widget.DiffUtil
import myapplication.android.mindall.common.delegate.DelegateItem

class ImportantDateDelegateItem(val value: ImportantDateModel) : DelegateItem {

    override fun content() = value

    override fun id() = value.hashCode()
}