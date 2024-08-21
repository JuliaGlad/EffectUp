package myapplication.android.mindall.common.recyclerView.weekDay

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.mindall.R
import myapplication.android.mindall.common.recyclerView.small_flag.SmallTaskFlagAdapter
import myapplication.android.mindall.common.recyclerView.small_flag.SmallTaskFlagModel
import myapplication.android.mindall.common.recyclerView.task_flags.TaskFlagAdapter
import myapplication.android.mindall.common.recyclerView.task_flags.TaskFlagModel
import myapplication.android.mindall.databinding.RecyclerViewWeekDayItemBinding

class WeekDayItemAdapter : ListAdapter<WeekDayItemModel, RecyclerView.ViewHolder>(WeekDayItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(RecyclerViewWeekDayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false).root)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position))
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding: RecyclerViewWeekDayItemBinding = RecyclerViewWeekDayItemBinding.bind(itemView)

        fun bind(model: WeekDayItemModel){

            setWeekDayTitle(model.dayOfWeek)
            binding.weekTitle.date.text = model.date

            binding.item.setOnClickListener {
                model.listener.onClick()
            }

            initRecyclerView(model.tasks)
        }

        private fun setWeekDayTitle(dayOfWeek: String) {
            val daysOfWeek = itemView.resources.getStringArray(R.array.week_days)
            binding.weekTitle.weekDayTitle.text =
                when(dayOfWeek){
                    "Mon" -> daysOfWeek[0]
                    "Tue" -> daysOfWeek[1]
                    "Wed" -> daysOfWeek[2]
                    "Thu" -> daysOfWeek[3]
                    "Fri" -> daysOfWeek[4]
                    "Sat" -> daysOfWeek[5]
                    "Sun" -> daysOfWeek[6]
                    else -> null
                }
        }

        private fun initRecyclerView(tasks: List<SmallTaskFlagModel>) {
            val adapter = SmallTaskFlagAdapter()
            binding.dayTasks.adapter = adapter
            adapter.submitList(tasks)
        }
    }
}