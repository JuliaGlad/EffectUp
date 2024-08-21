package myapplication.android.mindall.presentation.plans.pomodoro.pomodoroTimer

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import myapplication.android.mindall.R
import myapplication.android.mindall.common.Constants.Companion.IS_POMODORO_STARTED
import myapplication.android.mindall.common.Constants.Companion.IS_WORK
import myapplication.android.mindall.common.Constants.Companion.PAUSED_HOURS
import myapplication.android.mindall.common.Constants.Companion.PAUSED_HOURS_REST
import myapplication.android.mindall.common.Constants.Companion.PAUSED_MINUTES
import myapplication.android.mindall.common.Constants.Companion.PAUSED_MINUTES_REST
import myapplication.android.mindall.common.Constants.Companion.PAUSED_SECOND
import myapplication.android.mindall.common.Constants.Companion.PAUSED_SECOND_REST
import myapplication.android.mindall.common.Constants.Companion.POMODORO_TURN
import myapplication.android.mindall.common.Constants.Companion.PREFERENCES
import myapplication.android.mindall.common.Constants.Companion.TIME_MILLIS_LEFT_REST
import myapplication.android.mindall.common.Constants.Companion.TIME_MILLIS_LEFT_WORK
import myapplication.android.mindall.common.Constants.Companion.TIME_MILLIS_REST_PRIMARY
import myapplication.android.mindall.common.Constants.Companion.TIME_MILLIS_WORK_PRIMARY
import myapplication.android.mindall.databinding.FragmentPomodoroTimerBinding
import java.util.Locale

class PomodoroTimerFragment : Fragment() {
    private val viewModel: PomodoroTimerViewModel by viewModels()
    private var timeMillisWorkPrimary: Long = 0
    private var timeMillisRestPrimary: Long = 0
    private var timeMillisWork: Long = 0
    private var timeMillisRest: Long = 0
    private var isWorkTime: Boolean = true
    private var isStarted: Boolean = false
    private lateinit var preferences: SharedPreferences
    private lateinit var binding: FragmentPomodoroTimerBinding
    private var timeLeft: String = 0.toString()
    private var timeMillisLeftWork: Long = 0
    private var timeMillisLeftRest: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferences = activity?.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)!!

        if (arguments != null) {
            val (hoursWork, minutesWork, secondsWork) = getWorkTime()
            val (hoursRest, minutesRest, secondsRest) = getPausedTime()

            if (hoursWork != null && minutesWork != null && secondsWork != null) {
                timeMillisWork = hoursWork + minutesWork + secondsWork
                timeMillisWorkPrimary = timeMillisWork
                timeMillisLeftWork = timeMillisWork
            }

            if (hoursRest != null && minutesRest != null && secondsRest != null) {
                timeMillisRest = hoursRest + minutesRest + secondsRest
                timeMillisRestPrimary = timeMillisRest
                timeMillisLeftRest = timeMillisRest
            }
        } else {
            getPreferences()
        }
    }

    private fun getPreferences() {
        isStarted = preferences.getBoolean(IS_POMODORO_STARTED, false)
        if (isStarted) {
            preferences.getBoolean(IS_WORK, false).let { isWorkTime = it }
            preferences.getLong(TIME_MILLIS_LEFT_WORK, 0).let {
                timeMillisWork = it
                timeMillisLeftWork = it
            }
            preferences.getLong(TIME_MILLIS_REST_PRIMARY, 0).let { timeMillisRestPrimary = it }
            preferences.getLong(TIME_MILLIS_WORK_PRIMARY, 0).let { timeMillisWorkPrimary = it }
            preferences.getLong(TIME_MILLIS_LEFT_REST, 0).let {
                timeMillisRest = it
                timeMillisLeftRest = it
            }
            preferences.getInt(POMODORO_TURN, 0).let { viewModel.setValue(it) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPomodoroTimerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserves()
        initTextBox()
        initBackButton()
        initStopButton()
        if (!isStarted) {
            startTimer()
        }
        initTitle()
    }

    private fun initTitle() {
        if (isWorkTime) {
            binding.title.text = getString(R.string.time_to_work)
        } else {
            binding.title.text = getString(R.string.time_to_rest)
        }
    }

    private fun startTimer() {
        if (isWorkTime) {
            binding.title.text = getString(R.string.time_to_work)
            startWorkCountDown()
        } else {
            binding.title.text = getString(R.string.time_to_rest)
            startRestCountDown()
        }
    }

    private fun initTextBox() {
        binding.layoutBox.body.text = getString(R.string.pomodoro_timer_will_repeat_4_times)
    }

    private fun initStopButton() {
        binding.buttonStopPause.setOnClickListener {
            setSettingsNull()
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_pomodoroTimerFragment_to_chooseTimeFragment)
        }
    }

    private fun initBackButton() {
        binding.buttonBack.setOnClickListener {
            setSettings()
            activity?.finish()
        }
    }

    private fun setSettings() {
        preferences.edit()?.apply {
            putBoolean(IS_WORK, isWorkTime)
            putBoolean(IS_POMODORO_STARTED, true)
            putLong(TIME_MILLIS_LEFT_WORK, timeMillisLeftWork)
            putLong(TIME_MILLIS_LEFT_REST, timeMillisLeftRest)
            putLong(TIME_MILLIS_REST_PRIMARY, timeMillisRestPrimary)
            putLong(TIME_MILLIS_WORK_PRIMARY, timeMillisWorkPrimary)

            Log.i("Applied", "edmededje")
        }?.apply()
        viewModel.getTurn()?.let {
            preferences.edit()?.putInt(POMODORO_TURN, it)?.apply()
        }
    }

    private fun setupObserves() {
        viewModel.turn.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it != 4) {
                    if (!isStarted) {
                        timeMillisWork = timeMillisWorkPrimary
                        timeMillisRest = timeMillisRestPrimary
                        startWorkCountDown()
                    } else {
                        isStarted = false
                        if (!isWorkTime) {
                            startRestCountDown()
                        } else {
                            startWorkCountDown()
                        }
                    }
                } else {
                    setSettingsNull()
                    NavHostFragment.findNavController(this)
                        .navigate(R.id.action_pomodoroTimerFragment_to_pomodoroFinishedFragment)
                }
            }
        }
    }

    private fun setSettingsNull() {
        preferences.edit()?.apply {
            putBoolean(IS_POMODORO_STARTED, false)
            putLong(TIME_MILLIS_LEFT_WORK, 0)
            putLong(TIME_MILLIS_LEFT_REST, 0)
            putLong(TIME_MILLIS_REST_PRIMARY, 0)
            putLong(TIME_MILLIS_WORK_PRIMARY, 0)
            putInt(POMODORO_TURN, 0)
        }?.apply()
    }

    private fun getPausedTime(): Triple<Long?, Long?, Long?> {
        val hoursRest = arguments?.getInt(PAUSED_HOURS_REST)?.times(3600000L)
        val minutesRest = arguments?.getInt(PAUSED_MINUTES_REST)?.times(60000L)
        val secondsRest = arguments?.getInt(PAUSED_SECOND_REST)?.times(1000L)
        return Triple(hoursRest, minutesRest, secondsRest)
    }

    private fun getWorkTime(): Triple<Long?, Long?, Long?> {
        val hoursWork = arguments?.getInt(PAUSED_HOURS)?.times(3600000L)
        val minutesWork = arguments?.getInt(PAUSED_MINUTES)?.times(60000L)
        val secondsWork = arguments?.getInt(PAUSED_SECOND)?.times(1000L)
        return Triple(hoursWork, minutesWork, secondsWork)
    }

    private fun startWorkCountDown() {
        isWorkTime = true
        val progressMax = timeMillisWorkPrimary.toInt()
        binding.indicator.max = progressMax
        val progress = timeMillisLeftWork.toInt()

        binding.indicator.setProgress(progress, true)

        object : CountDownTimer(timeMillisWork, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeMillisWork = millisUntilFinished
                binding.indicator.progress = (timeMillisWork - 1000).toInt()
                updateWorkTimer()
            }

            override fun onFinish() {
                binding.title.text = getString(R.string.time_to_rest)
                startRestCountDown()
            }
        }.start()
    }

    private fun startRestCountDown() {
        isWorkTime = false
        val progressMax = timeMillisRestPrimary.toInt()
        binding.indicator.max = progressMax

        val progress = timeMillisLeftRest.toInt()

        binding.indicator.setProgress(progress, true)

        if (timeMillisRest < 1000){
            timeMillisRest = timeMillisRestPrimary
        }

        object : CountDownTimer(timeMillisRest, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeMillisRest = millisUntilFinished
                binding.indicator.setProgress((timeMillisRest - 1000).toInt(), true)
                updateRestTimer()
            }

            override fun onFinish() {
                binding.title.text = getString(R.string.time_to_work)
                viewModel.increaseValue()
            }
        }.start()
    }

    private fun updateWorkTimer() {
        val hours: Long = (timeMillisWork / 1000) / 3600
        val minutes: Long = ((timeMillisWork / 1000) % 3600) / 60
        val seconds: Long = (timeMillisWork / 1000) % 60
        timeMillisLeftWork = timeMillisWork

        timeLeft = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
        binding.timer.text = timeLeft
    }

    private fun updateRestTimer() {
        val hours: Long = (timeMillisRest / 1000) / 3600
        val minutes: Long = ((timeMillisRest / 1000) % 3600) / 60
        val seconds: Long = (timeMillisRest / 1000) % 60

        timeMillisLeftRest = timeMillisRest

        timeLeft = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
        binding.timer.text = timeLeft
    }
}