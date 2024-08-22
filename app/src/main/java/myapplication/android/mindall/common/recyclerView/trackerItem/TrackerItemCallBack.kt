package myapplication.android.mindall.common.recyclerView.trackerItem

import androidx.recyclerview.widget.DiffUtil

class TrackerItemCallBack : DiffUtil.ItemCallback<TrackerModel>() {
    override fun areItemsTheSame(oldItem: TrackerModel, newItem: TrackerModel) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TrackerModel, newItem: TrackerModel) = oldItem.compareTo(newItem)
}