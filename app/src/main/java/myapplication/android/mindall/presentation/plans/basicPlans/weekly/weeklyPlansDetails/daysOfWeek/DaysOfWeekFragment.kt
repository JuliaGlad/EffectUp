package myapplication.android.mindall.presentation.plans.basicPlans.weekly.weeklyPlansDetails.daysOfWeek

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import myapplication.android.mindall.R
import myapplication.android.mindall.common.Constants.Companion.DATA
import myapplication.android.mindall.common.Constants.Companion.DAYS
import myapplication.android.mindall.common.Constants.Companion.DAY_ID
import myapplication.android.mindall.common.Constants.Companion.DAY_OF_WEEK
import myapplication.android.mindall.common.Constants.Companion.DURATION
import myapplication.android.mindall.common.Constants.Companion.IS_NEW
import myapplication.android.mindall.common.Constants.Companion.WEEKLY_PLAN_ID
import myapplication.android.mindall.common.Status
import myapplication.android.mindall.common.listeners.ButtonClickListener
import myapplication.android.mindall.common.recyclerView.small_flag.SmallTaskFlagModel
import myapplication.android.mindall.common.recyclerView.weekDay.WeekDayItemAdapter
import myapplication.android.mindall.common.recyclerView.weekDay.WeekDayItemModel
import myapplication.android.mindall.databinding.FragmentDaysOfWeekBinding
import myapplication.android.mindall.presentation.plans.basicPlans.weekly.model.DayOfWeekPresModel
import myapplication.android.mindall.presentation.plans.basicPlans.weekly.model.DayOfWeekShortModel
import java.util.Random

class DaysOfWeekFragment : Fragment() {
    private lateinit var binding: FragmentDaysOfWeekBinding
    private val viewModel: DaysOfWeekViewModel by viewModels()
    private var weeklyPlansId: String? = null
    private var week: String? = null
    private var isNew: Boolean? = false
    private var daysFullData: List<DayOfWeekPresModel> = mutableListOf()
    private lateinit var daysShortData: List<DayOfWeekShortModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weeklyPlansId = arguments?.getString(WEEKLY_PLAN_ID)
        week = arguments?.getString(DURATION)
        isNew = arguments?.getBoolean(IS_NEW, false)
        daysShortData = Gson().fromJson(
            arguments?.getString(DAYS),
            object : TypeToken<List<DayOfWeekShortModel>>() {}.type
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDaysOfWeekBinding.inflate(layoutInflater)
        viewModel.getWeekDaysData(weeklyPlansId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtonBack()
        setupObserves()
    }

    private fun setupObserves() {
        viewModel.state.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it.status) {
                    Status.SUCCESS -> {
                        binding.titleCustom.text = week

                        val daysNotSorted = it.data ?: getDaysFromShortData(daysShortData)
                        daysFullData = sortDaysOfWeek(daysNotSorted)

                        initRecycler(daysFullData)
                        binding.loading.root.visibility = View.GONE
                    }

                    Status.ERROR -> {}
                    Status.LOADING -> {
                        binding.loading.root.visibility = View.VISIBLE
                    }
                }
            }
        }
    }


    private fun sortDaysOfWeek(days: List<DayOfWeekPresModel>): List<DayOfWeekPresModel> {
        val sortedDays = days.sortedWith { firstDay, secondDay ->
            firstDay.index.compareTo(secondDay.index)
        }
        return sortedDays
    }

    private fun getDaysFromShortData(shortData: List<DayOfWeekShortModel>): List<DayOfWeekPresModel> {
        val days: MutableList<DayOfWeekPresModel> = mutableListOf()
        for (i in shortData) {
            days.add(
                DayOfWeekPresModel(
                    generateId(),
                    i.dayOfWeek,
                    i.date,
                    "0",
                    "0",
                    "0"
                )
            )
        }
        return days
    }

    private fun generateId(): String {
        val rand = Random()
        val id = rand.nextInt(90000000) + 10000000
        return "@$id"
    }

    private fun initFlags(
        greenTaskCount: String?,
        yellowTaskCount: String?,
        redTaskCount: String?
    ) = listOf(
        SmallTaskFlagModel(
            1,
            R.drawable.ic_green_flag,
            greenTaskCount,
            R.color.green_flag_color
        ),
        SmallTaskFlagModel(
            2,
            R.drawable.ic_yellow_flag,
            yellowTaskCount,
            R.color.yellow_flag_color
        ),
        SmallTaskFlagModel(
            3,
            R.drawable.ic_red_flag,
            redTaskCount,
            R.color.red_flag_color
        ),
    )

    private fun initRecycler(days: List<DayOfWeekPresModel>) {
        val adapter = WeekDayItemAdapter()
        val items = mutableListOf<WeekDayItemModel>()

        for (i in days) {
            weeklyPlansId?.let {
                items.addWeekItem(
                    days.indexOf(i),
                    daysFullData,
                    this,
                    it,
                    i.id,
                    i.dayOfWeek.toString(),
                    i.date.toString(),
                    week.toString(),
                    isNew,
                    initFlags(i.greenTaskCount, i.yellowTaskCount, i.redTaskCount)
                )
            }
        }

        binding.recyclerView.adapter = adapter
        adapter.submitList(items)
    }

    private fun initButtonBack() {
        binding.buttonBack.setOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
        }
    }
}

private val DayOfWeekPresModel.index: Int
    get() = when (this.dayOfWeek) {
        "Mon" -> 0
        "Tue" -> 1
        "Wed" -> 2
        "Thu" -> 3
        "Fri" -> 4
        "Sat" -> 5
        "Sun" -> 6
        else -> 7
    }

private fun MutableList<WeekDayItemModel>.addWeekItem(
    index: Int,
    days: List<DayOfWeekPresModel>,
    fragment: Fragment,
    weeklyPlanId: String,
    dayId: String,
    dayOfWeek: String,
    date: String,
    week: String,
    isNew: Boolean?,
    tasks: List<SmallTaskFlagModel>
) {
    this.add(
        WeekDayItemModel(
            index,
            dayOfWeek,
            date,
            tasks,
            object : ButtonClickListener {
                override fun onClick() {
                    val bundle = Bundle().apply {
                        putString(WEEKLY_PLAN_ID, weeklyPlanId)
                        putString(DAY_ID, dayId)
                        putString(DATA, date)
                        putString(DAY_OF_WEEK, dayOfWeek)
                        putString(DURATION, week)
                        isNew?.let { putBoolean(IS_NEW, it)}
                        putString(DAYS, Gson().toJson(days))
                    }
                    NavHostFragment.findNavController(fragment)
                        .navigate(
                            R.id.action_daysOfWeekFragment_to_weeklyTaskDetailsFragment,
                            bundle
                        )
                }
            })
    )
}
