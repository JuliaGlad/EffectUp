package myapplication.android.mindall.presentation.plans.pomodoro.chooseTime

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import myapplication.android.mindall.R
import myapplication.android.mindall.common.Constants
import myapplication.android.mindall.common.Constants.Companion.IS_POMODORO_STARTED
import myapplication.android.mindall.common.Constants.Companion.PAUSED_HOURS
import myapplication.android.mindall.common.Constants.Companion.PAUSED_MINUTES
import myapplication.android.mindall.common.Constants.Companion.PAUSED_SECOND
import myapplication.android.mindall.common.Constants.Companion.PREFERENCES
import myapplication.android.mindall.common.listeners.ButtonClickListener
import myapplication.android.mindall.common.listeners.DialogDismissedListener
import myapplication.android.mindall.common.recyclerView.timeItem.TimeChooseAdapter
import myapplication.android.mindall.common.recyclerView.timeItem.TimeChooseModel
import myapplication.android.mindall.databinding.FragmentChooseTimeBinding
import myapplication.android.mindall.presentation.dialog.chooseTime.ChooseTimeRestDialogFragment
import myapplication.android.mindall.presentation.dialog.chooseTime.ChooseWorkTimeDialogFragment

class ChooseTimeFragment : Fragment() {
    private lateinit var binding: FragmentChooseTimeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref = activity?.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        val isStarted = sharedPref?.getBoolean(IS_POMODORO_STARTED, false)
        if (isStarted == true) {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_chooseTimeFragment_to_pomodoroTimerFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChooseTimeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBackButton()
        initRecycler()
    }

    private fun navigate(bundle: Bundle) {
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_chooseTimeFragment_to_pomodoroTimerFragment, bundle)
    }

    private fun initRecycler() {
        val adapter = TimeChooseAdapter()
        binding.timeRecyclerView.adapter = adapter

        val items = listOf(
            TimeChooseModel(
                1,
                "25 : 00",
                object : ButtonClickListener {
                    override fun onClick() {
                        navigate(Bundle().apply {
                            putWorkTimeData(0, 25, 0)
                            putRestTimeData(0, 5, 0)
                        })
                    }
                }
            ),
            TimeChooseModel(
                2,
                "45 : 00",
                object : ButtonClickListener {
                    override fun onClick() {
                        navigate(Bundle().apply {
                            putWorkTimeData(0, 45, 0)
                            putRestTimeData(0, 10, 0)
                        })
                    }
                }
            ),
            TimeChooseModel(
                3,
                "60 : 00",
                object : ButtonClickListener {
                    override fun onClick() {
                        navigate(Bundle().apply {
                            putWorkTimeData(1, 0, 0)
                            putRestTimeData(0, 15, 0)
                        })
                    }
                }
            ),
            TimeChooseModel(
                4,
                getString(R.string.your_time),
                object : ButtonClickListener {
                    override fun onClick() {
                        showWorkDialog()
                    }
                }
            )
        )

        adapter.submitList(items)
    }

    private fun showWorkDialog() {
        val dialogFragment = ChooseWorkTimeDialogFragment()
        activity?.supportFragmentManager?.let { dialogFragment.show(it, "CHOOSE_TIME_DIALOG") }
        dialogFragment.onDismissListener(object : DialogDismissedListener {
            override fun handleDialogClose(bundle: Bundle?) {
                bundle?.let {
                    showRestDialog(it)
                }
            }
        })
    }

    private fun showRestDialog(bundleWork: Bundle) {
        val dialogFragmentRest = chooseRestTimeDialogFragment()
        dialogFragmentRest.onDismissListener(object : DialogDismissedListener {
            override fun handleDialogClose(bundle: Bundle?) {
                bundle?.let { bundleChecked ->

                    val (secondsWork, minutesWork, hoursWork) = getTime(bundleWork)
                    val (secondsRest, minutesRest, hoursRest) = getTime(bundleChecked)

                    navigate(Bundle().apply {
                        putWorkTimeData(hoursWork, minutesWork, secondsWork)
                        putRestTimeData(hoursRest, minutesRest, secondsRest)
                    })
                }
            }
        })
    }

    private fun getTime(bundleWork: Bundle): Triple<Int, Int, Int> {
        val secondsWork = bundleWork.getInt(PAUSED_SECOND)
        val minutesWork = bundleWork.getInt(PAUSED_MINUTES)
        val hoursWork = bundleWork.getInt(PAUSED_HOURS)
        return Triple(secondsWork, minutesWork, hoursWork)
    }

    private fun chooseRestTimeDialogFragment(): ChooseTimeRestDialogFragment {
        val dialogFragmentRest = ChooseTimeRestDialogFragment()
        activity?.supportFragmentManager?.let { rest ->
            dialogFragmentRest.show(
                rest,
                "CHOOSE_TIME_DIALOG"
            )
        }
        return dialogFragmentRest
    }

    private fun Bundle.putWorkTimeData(workHours: Int, workMinutes: Int, workSeconds: Int) {
        putInt(PAUSED_HOURS, workHours)
        putInt(PAUSED_MINUTES, workMinutes)
        putInt(PAUSED_SECOND, workSeconds)
    }

    private fun Bundle.putRestTimeData(restHours: Int, restMinutes: Int, restSeconds: Int) {
        putInt(Constants.PAUSED_HOURS_REST, restHours)
        putInt(Constants.PAUSED_MINUTES_REST, restMinutes)
        putInt(Constants.PAUSED_SECOND_REST, restSeconds)
    }

    private fun initBackButton() {
        binding.buttonBack.setOnClickListener {
            activity?.finish()
        }
    }
}