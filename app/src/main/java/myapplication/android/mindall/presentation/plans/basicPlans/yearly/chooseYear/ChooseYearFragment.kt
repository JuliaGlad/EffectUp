package myapplication.android.mindall.presentation.plans.basicPlans.yearly.chooseYear

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import myapplication.android.mindall.R
import myapplication.android.mindall.common.Constants.Companion.IS_NEW
import myapplication.android.mindall.common.Constants.Companion.YEAR
import myapplication.android.mindall.common.Constants.Companion.YEAR_ID
import myapplication.android.mindall.common.listeners.ButtonClickListener
import myapplication.android.mindall.common.recyclerView.yearItem.YearItemAdapter
import myapplication.android.mindall.common.recyclerView.yearItem.YearItemModel
import myapplication.android.mindall.databinding.FragmentChooseYearBinding
import kotlin.properties.Delegates

class ChooseYearFragment : Fragment() {

    private lateinit var binding: FragmentChooseYearBinding
    private val viewModel: ChooseYearViewModel by viewModels()

    private lateinit var year : String
    private lateinit var yearId: String
    private var isNew by Delegates.notNull<Boolean>()

    private val yearAdapter = YearItemAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChooseYearBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        setupObserves()
        initButtonBack()
        initDetailsButton()
    }

    private fun setupObserves() {
        viewModel.yearId.observe(viewLifecycleOwner){
            if (it != null){
                yearId = it
            }
        }

        viewModel.isNew.observe(viewLifecycleOwner){
            if (it != null){
                isNew = it
                navigateToDetailsFragment()
                viewModel.setValueNull()
            }
        }
    }

    private fun navigateToDetailsFragment() {
        val bundle = Bundle().apply {
            putBoolean(IS_NEW, isNew)
            putString(YEAR, year)
            putString(YEAR_ID, yearId)
        }

        NavHostFragment.findNavController(this)
            .navigate(R.id.action_chooseYearFragment_to_yearlyGoalsDetailsFragment, bundle)
    }

    private fun initDetailsButton() {
        binding.buttonDetails.setOnClickListener {
            viewModel.getYearId(year)
        }
    }

    private fun initButtonBack() {
        binding.buttonBack.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun initRecycler() {
        binding.recyclerView.adapter = yearAdapter
        val yearStart = 2024
        val years = mutableListOf<YearItemModel>()
        for (i in yearStart..3000) {
            addItem(years, i)
        }
        yearAdapter.submitList(years)
    }

    private fun addItem(
        years: MutableList<YearItemModel>,
        i: Int
    ) {
        years.add(
            YearItemModel(
                years.size,
                i.toString(),
                false,
                object : ButtonClickListener {
                    override fun onClick() {
                        checkChosen(years, i)
                    }
                }
            )
        )
    }

    private fun checkChosen(
        years: MutableList<YearItemModel>,
        i: Int
    ) {
        for (j in years) {
            if (j.year != i.toString()) {
                j.isChosen = false
                yearAdapter.notifyItemChanged(years.indexOf(j))
            } else {
                year = i.toString()
                //  viewModel.getMonthId(month, yearId)
                j.isChosen = true
                yearAdapter.notifyItemChanged(years.indexOf(j))
            }
        }
    }
}