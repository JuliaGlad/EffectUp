package myapplication.android.mindall.presentation.plans.basicPlans.monthly.chooseYearAndMonth

import android.os.Bundle
import android.util.MonthDisplayHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import myapplication.android.mindall.R
import myapplication.android.mindall.common.Constants.Companion.IS_NEW
import myapplication.android.mindall.common.Constants.Companion.MONTH
import myapplication.android.mindall.common.Constants.Companion.MONTH_ID
import myapplication.android.mindall.common.Constants.Companion.YEAR
import myapplication.android.mindall.common.Constants.Companion.YEAR_ID
import myapplication.android.mindall.common.listeners.ButtonClickListener
import myapplication.android.mindall.common.recyclerView.monthItem.MonthItemAdapter
import myapplication.android.mindall.common.recyclerView.monthItem.MonthItemModel
import myapplication.android.mindall.databinding.FragmentChooseYearAndMonthBinding

class ChooseYearAndMonthFragment : Fragment() {
    private val adapter = MonthItemAdapter()
    private lateinit var binding: FragmentChooseYearAndMonthBinding
    private val viewModel: ChooseYearAndMonthViewModel by viewModels()

    private var isNew: Boolean? = false
    private var isFirstList = true
    private lateinit var year: String
    private lateinit var yearId: String
    private lateinit var month: String
    private lateinit var monthId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        year = 2024.toString()
        viewModel.getYearId(year)
        initMonth(year.toInt())

        binding = FragmentChooseYearAndMonthBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setNumberPickerOptions()
        initNumberPicker()
        setupObserves()
        initBackButton()
        initDetailsButton()
    }

    private fun setNumberPickerOptions() {
        binding.numbers.minValue = 2024
        binding.numbers.maxValue = 3000
        binding.numbers.wrapSelectorWheel = false
        binding.numbers.value = binding.numbers.minValue
    }

    private fun setupObserves() {
        viewModel.yearId.observe(viewLifecycleOwner) {
            if (it != null) {
                yearId = it
            }
        }

        viewModel.isNew.observe(viewLifecycleOwner) {
            if (it != null) {
                isNew = it
            }
        }

        viewModel.monthId.observe(viewLifecycleOwner) {
            if (it != null) {
                monthId = it
            }
        }
    }

    private fun initDetailsButton() {
        binding.buttonDetails.setOnClickListener {

            val bundle = Bundle().apply {
                isNew?.let { value -> putBoolean(IS_NEW, value) }
                putString(YEAR, year)
                putString(YEAR_ID, yearId)
                putString(MONTH, month)
                putString(MONTH_ID, monthId)
            }

            NavHostFragment.findNavController(this)
                .navigate(
                    R.id.action_chooseYearAndMonthFragment_to_monthPlanDetailsFragment,
                    bundle
                )
        }
    }

    private fun initBackButton() {
        binding.buttonBack.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun setAdapter() {
        binding.recyclerView.adapter = adapter
    }

    private fun initNumberPicker() {
        binding.numbers.setOnValueChangedListener { _, _, newVal ->
            year = newVal.toString()
            viewModel.getYearId(year)
            initMonth(newVal)
        }
    }

    private fun initMonth(year: Int) {
        val months = mutableListOf<MonthItemModel>()
        for (i in 0..11) {
            val monthDisplayHelper = MonthDisplayHelper(year, i)
            var monthNumber = (monthDisplayHelper.month + 1).toString()

            if (monthNumber.toInt() < 10){
                monthNumber = "0$monthNumber"
            }

            val monthEnd = monthDisplayHelper.numberOfDaysInMonth
            val duration = "01.$monthNumber - $monthEnd.$monthNumber"
            addMonthItem(months, i, monthDisplayHelper, duration)
        }
        adapter.submitList(months)
        if (!isFirstList) {
            adapter.onCurrentListChanged(adapter.currentList, months)
        } else {
            isFirstList = false
        }
    }

    private fun addMonthItem(
        months: MutableList<MonthItemModel>,
        i: Int,
        monthDisplayHelper: MonthDisplayHelper,
        duration: String
    ) {
        val isChosen = false
        val monthCurrent = getMonth(monthDisplayHelper.month)
        months.add(MonthItemModel(
            i,
            monthCurrent,
            duration,
            isChosen,
            object : ButtonClickListener {
                override fun onClick() {
                    checkChosen(months, monthCurrent)
                }
            }
        ))
    }

    private fun checkChosen(
        months: MutableList<MonthItemModel>,
        monthCurrent: String
    ) {
        for (j in months) {
            if (j.month != monthCurrent) {
                j.isChosen = false
                adapter.notifyItemChanged(months.indexOf(j))
            } else {
                month = monthCurrent
                viewModel.getMonthId(month, yearId)
                j.isChosen = true
                adapter.notifyItemChanged(months.indexOf(j))
            }
        }
    }

    private fun getMonth(monthNumber: Int): String {
        val months = resources.getStringArray(R.array.month)
        return when (monthNumber) {
            0 -> months[0]
            1 -> months[1]
            2 -> months[2]
            3 -> months[3]
            4 -> months[4]
            5 -> months[5]
            6 -> months[6]
            7 -> months[7]
            8 -> months[8]
            9 -> months[9]
            10 -> months[10]
            11 -> months[11]
            else -> "No month"
        }
    }
}