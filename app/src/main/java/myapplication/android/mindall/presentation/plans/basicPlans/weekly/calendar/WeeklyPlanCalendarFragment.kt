package myapplication.android.mindall.presentation.plans.basicPlans.weekly.calendar

import android.R.attr.firstDayOfWeek
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
import com.google.gson.Gson
import myapplication.android.mindall.R
import myapplication.android.mindall.common.Constants.Companion.DATA
import myapplication.android.mindall.common.Constants.Companion.DAYS
import myapplication.android.mindall.common.Constants.Companion.DURATION
import myapplication.android.mindall.common.Constants.Companion.ID
import myapplication.android.mindall.common.Constants.Companion.IS_NEW
import myapplication.android.mindall.common.Constants.Companion.WEEKLY_PLAN_ID
import myapplication.android.mindall.databinding.FragmentMonthlyPlanCalendarBinding
import myapplication.android.mindall.presentation.plans.basicPlans.weekly.model.DayOfWeekShortModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Random


class WeeklyPlanCalendarFragment : Fragment() {
    private lateinit var binding: FragmentMonthlyPlanCalendarBinding
    private val viewModel: WeeklyPlanCalendarViewModel by viewModels()
    private var weeklyPlanId: String? = null
    private var week: String? = null
    private var isNew: Boolean = false
    private val daysOfWeek = mutableListOf<DayOfWeekShortModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMonthlyPlanCalendarBinding.inflate(layoutInflater)
        getCurrentWeek()

        return binding.root
    }

    private fun getCurrentWeek() {
        val year = getCurrentYear()
        val month = getCurrentMonth()
        val day = getCurrentDate()

        val weekOfYear = getWeekOfYear(year, month, day)
        val currentWeek = getStartEndOFWeek(weekOfYear, year)
        viewModel.checkPlans(currentWeek)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewDetailsButton()
        setupObserves()
        initBackButton()
        initCalendar()
    }

    private fun initBackButton() {
        binding.buttonBack.setOnClickListener {
            activity?.finish()
        }
    }

    private fun initViewDetailsButton() {
        binding.buttonOk.setOnClickListener {
            if (weeklyPlanId == null){
                isNew = true
                weeklyPlanId = generateId()
            } else {
                isNew = false
            }

            val bundle = Bundle().apply {
                putString(WEEKLY_PLAN_ID, weeklyPlanId)
                putString(DAYS, Gson().toJson(daysOfWeek))
                putString(DURATION, week)
                putBoolean(IS_NEW, isNew)
            }

            NavHostFragment.findNavController(this)
                .navigate(R.id.action_monthlyPlanCalendarFragment_to_daysOfWeekFragment, bundle)
        }
    }

    private fun generateId() : String{
        val rand = Random()
        val id = rand.nextInt(90000000) + 10000000
        Log.i("Data", "@$id")
        return "@$id"
    }

    private fun setupObserves() {
        viewModel.havePlans.observe(viewLifecycleOwner) {
            if (it != null) {
                var isFound = false
                for (key in it.keySet()) {
                    Log.i("Key", key.toString())
                    if (key.toString() == ID) {
                        weeklyPlanId = it.getString(ID)
                        isFound = true
                        binding.infoBoxLoading.loadingLayout.visibility = GONE
                        val text = getString(R.string.you_already_have_plans_for)  + " " + it.getString(DATA)
                        binding.infoBox.body.text = text
                    }
                }
                if (!isFound) {
                    binding.infoBoxLoading.loadingLayout.visibility = GONE
                    val text =
                        getString(R.string.you_dont_have_any_plans) + " " + it.getString(DATA)
                    binding.infoBox.body.text = text
                }
            } else {
                binding.infoBoxLoading.root.visibility = VISIBLE
            }
        }
    }

    private fun initCalendar() {
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val weekOfYear = getWeekOfYear(year, month, dayOfMonth)
            week = getStartEndOFWeek(weekOfYear, year)
            viewModel.checkPlans(week!!)
        }
    }

    private fun getWeekOfYear(year: Int, month: Int, dayOfMonth: Int): Int {
        val calendar: Calendar = Calendar.getInstance(Locale.getDefault())
        calendar[year, month] = dayOfMonth
        return calendar[Calendar.WEEK_OF_YEAR]
    }

    private fun getStartEndOFWeek(week: Int, year: Int): String {

        val calendar = Calendar.getInstance()
        calendar.clear()
        calendar[Calendar.WEEK_OF_YEAR] = week
        calendar[Calendar.YEAR] = year
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        val startDate = calendar.time
        val startDateInStr = formatter.format(startDate)

        calendar.add(Calendar.DATE, 6)

        val endDate = calendar.time
        val endDateString = formatter.format(endDate)

        if (daysOfWeek.isNotEmpty()){
            daysOfWeek.clear()
        }

        for (i in 0..6) {
            calendar[Calendar.DAY_OF_WEEK] = firstDayOfWeek + i

            val weekDay = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH)
            val day = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.time)

            weekDay?.let { DayOfWeekShortModel(day, it) }?.let { daysOfWeek.add(it) }
        }


        return "$startDateInStr - $endDateString"
    }

    private fun getCurrentDate(): Int {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return day
    }

    private fun getCurrentMonth(): Int {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH) + 1

        return month
    }

    private fun getCurrentYear(): Int {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)

        return year
    }

}