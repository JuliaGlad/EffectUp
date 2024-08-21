package myapplication.android.mindall.presentation.plans.basicPlans.daily.dailyPlanDetails.addSchedule

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import myapplication.android.mindall.common.Constants.Companion.DAILY_PLAN_ID
import myapplication.android.mindall.common.Constants.Companion.DURATION
import myapplication.android.mindall.common.Constants.Companion.HOURS
import myapplication.android.mindall.common.Constants.Companion.IS_NOTIFICATION_ON
import myapplication.android.mindall.common.Constants.Companion.MINUTES
import myapplication.android.mindall.common.Constants.Companion.TIME
import myapplication.android.mindall.common.Constants.Companion.TITLE
import myapplication.android.mindall.common.listeners.DialogDismissedListener
import myapplication.android.mindall.databinding.FragmentAddScheduleBinding
import myapplication.android.mindall.presentation.dialog.chooseTimeDialog.ChooseTimeDialogFragment


class AddScheduleFragment : Fragment() {

    private lateinit var dailyPlanId: String
    private lateinit var binding: FragmentAddScheduleBinding
    private var startTime: String? = null
    private var endTime: String? = null
    private var title: String? = null
    private var isNotificationOn: Boolean = false
    private val viewModel : AddScheduleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dailyPlanId = activity?.intent?.getStringExtra(DAILY_PLAN_ID).toString()
        Log.i("Chose Date", dailyPlanId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddScheduleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserves()
        initStartButton()
        initEndButton()
        initAddButton()
    }

    private fun setupObserves() {
        viewModel.isAdded.observe(viewLifecycleOwner){
            if (it){
                val intent = Intent().apply{
                    putExtra(TITLE, title)
                    putExtra(DURATION, "$startTime - $endTime")
                    putExtra(IS_NOTIFICATION_ON, isNotificationOn)
                }
                activity?.apply {
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    private fun initAddButton() {
        binding.buttonAdd.setOnClickListener {
            title = binding.editLayoutTitle.text.toString()
            viewModel.addSchedule(dailyPlanId, title, "$startTime - $endTime", isNotificationOn, startTime)
        }
    }

    private fun initEndButton() {
        binding.buttonEnd.setOnClickListener {
            val dialogFragment = ChooseTimeDialogFragment()
            activity?.supportFragmentManager?.let { dialogFragment.show(it, "CHOOSE_TIME_DIALOG_FRAGMENT")}
            dialogFragment.onDismissListener(object : DialogDismissedListener {
                override fun handleDialogClose(bundle: Bundle?) {
                    endTime = bundle?.getString(TIME)
                    val time = "$startTime - $endTime"
                    binding.timeLayout.title.text = time
                }
            })
        }
    }

    private fun initStartButton() {
        binding.buttonStart.setOnClickListener {
            val dialogFragment = ChooseTimeDialogFragment()
            activity?.supportFragmentManager?.let { dialogFragment.show(it, "CHOOSE_TIME_DIALOG_FRAGMENT")}
            dialogFragment.onDismissListener(object : DialogDismissedListener {
                override fun handleDialogClose(bundle: Bundle?) {
                    startTime = bundle?.getString(TIME)
                    val hours = bundle?.getString(HOURS)
                    val minutes = bundle?.getString(MINUTES)
                    if (endTime == null){
                        val plusHours = hours?.let { hour -> (Integer.parseInt(hour) + 1).toString()}
                        endTime = "$plusHours:$minutes"
                    }
                    val time = "$startTime - $endTime"
                    binding.timeLayout.title.text = time
                }
            })
        }
    }

}