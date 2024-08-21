package myapplication.android.mindall.presentation.plans.basicPlans.daily.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import myapplication.android.mindall.R
import myapplication.android.mindall.common.Constants.Companion.DAILY_PLAN_DATE
import myapplication.android.mindall.common.Constants.Companion.DAILY_PLAN_ID
import myapplication.android.mindall.common.Constants.Companion.IS_NEW
import myapplication.android.mindall.common.Status
import myapplication.android.mindall.common.recyclerView.task_flags.TaskFlagAdapter
import myapplication.android.mindall.common.recyclerView.task_flags.TaskFlagModel
import myapplication.android.mindall.databinding.FragmentCalendarBinding
import myapplication.android.mindall.presentation.plans.basicPlans.daily.model.DailyPlanModel
import java.util.Calendar
import java.util.Random

class CalendarFragment : Fragment() {
    private val viewModel: CalendarViewModel by viewModels()
    private lateinit var binding: FragmentCalendarBinding
    private lateinit var chosenDate: String
    private var chosenDateId: String? = null
    private var items: MutableList<TaskFlagModel> = mutableListOf()
    private var isNew = false
    private val adapter = TaskFlagAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        chosenDate = getCurrentDate()
        getCurrentDatePlansInfo(chosenDate)
        binding = FragmentCalendarBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setupObserves()
        initCalendar()
        initDetailsButton()
    }

    private fun initDetailsButton() {
        binding.detailsButton.setOnClickListener {

            if (chosenDateId == null) {
                isNew = true
                chosenDateId = generateId()
            } else {
                isNew = false
            }

            val bundle: Bundle = Bundle()
                .apply {
                    putBoolean(IS_NEW, isNew)
                    putString(DAILY_PLAN_ID, chosenDateId)
                    putString(DAILY_PLAN_DATE, chosenDate)
                }

            NavHostFragment.findNavController(this)
                .navigate(R.id.action_calendarFragment_to_dailyPlanDetailsFragment, bundle)
        }
    }

    private fun generateId() : String{
        val rand = Random()
        val id = rand.nextInt(90000000) + 10000000
        Log.i("Data", "@$id")
        return "@$id"
    }

    private fun setAdapter() {
        binding.recyclerView.adapter = adapter
    }

    private fun setupObserves() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    changeVisibility()
                    val model: DailyPlanModel? = it.data
                    if (model != null) {
                        chosenDateId = model.id
                        initRecycler(model)
                    }
                }

                Status.ERROR -> {

                }

                Status.LOADING -> {
                    binding.progressCardLayout.visibility = VISIBLE
                }
            }
        }

        viewModel.noPlan.observe(viewLifecycleOwner) {
            if (it) {
                binding.noTasks.visibility = VISIBLE
                binding.detailsButton.text = getString(R.string.add_tasks)
                items.clear()
                adapter.notifyItemRangeRemoved(0, 3)
            }
        }
    }

    private fun changeVisibility() {
        binding.progressCardLayout.visibility = GONE
        binding.noTasks.visibility = GONE
        binding.detailsButton.text = getString(R.string.view_details)
    }

    private fun initRecycler(model: DailyPlanModel) {
        items = mutableListOf(
            TaskFlagModel(
                1,
                R.drawable.ic_green_flag,
                model.greenFlagCount,
                R.color.green_flag_color
            ),
            TaskFlagModel(
                2,
                R.drawable.ic_yellow_flag,
                model.yellowFlagCount,
                R.color.yellow_flag_color
            ),
            TaskFlagModel(
                1,
                R.drawable.ic_red_flag,
                model.redFlagCount,
                R.color.red_flag_color
            )
        )
        adapter.submitList(items)
    }

    private fun initCalendar() {
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->

            val newMonth = month + 1
            chosenDate = "$dayOfMonth.$newMonth.$year"
            viewModel.setLoading()
            getCurrentDatePlansInfo(chosenDate)
        }
    }

    private fun getCurrentDatePlansInfo(date: String) {
        viewModel.getPlansInfo(date)
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return "$day.$month.$year"
    }
}