package myapplication.android.mindall.presentation.plans.basicPlans.choosePlans

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import myapplication.android.mindall.R
import myapplication.android.mindall.common.listeners.ButtonClickListener
import myapplication.android.mindall.common.recyclerView.buttonWithText.ButtonWithTextAdapter
import myapplication.android.mindall.common.recyclerView.buttonWithText.ButtonWithTextModel
import myapplication.android.mindall.databinding.FragmentChoosePlansBinding

class ChoosePlansTypeFragment : Fragment() {

    private lateinit var binding: FragmentChoosePlansBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChoosePlansBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initBackButton()
    }

    private fun initBackButton() {
        binding.imageButton.setOnClickListener {
            activity?.finish()
        }
    }

    private fun initRecyclerView() {
        val item: List<ButtonWithTextModel> = listOf(
            ButtonWithTextModel(1, getString(R.string.daily), getString(R.string.plan_your_day_with_effectup),
                object : ButtonClickListener{
                    override fun onClick() {
                        (activity as ChoosePlansActivity).openDailyPlansActivity()
                    }

                }),
            ButtonWithTextModel(2, getString(R.string.weekly), getString(R.string.set_goals_for_the_week_and_mark_the_tasks),
                object : ButtonClickListener{
                    override fun onClick() {
                        (activity as ChoosePlansActivity).openWeeklyPlansActivity()
                    }

                }),
            ButtonWithTextModel(3, getString(R.string.monthly), getString(R.string.set_goals_for_the_month_and_structure_your_tasks_and_plans),
                object : ButtonClickListener{
                    override fun onClick() {
                        (activity as ChoosePlansActivity).openMonthlyPlansActivity()
                    }

                }),
            ButtonWithTextModel(4, getString(R.string.yearly), getString(R.string.mark_your_annual_goals_important_data_and_other_notes),
                object : ButtonClickListener{
                    override fun onClick() {
                        (activity as ChoosePlansActivity).openYearlyPlansActivity()
                    }

                }),
        )
        val adapter = ButtonWithTextAdapter()
        binding.recyclerView.adapter = adapter
        adapter.submitList(item)
    }
}