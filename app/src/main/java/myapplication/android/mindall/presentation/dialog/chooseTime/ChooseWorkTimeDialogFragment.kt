package myapplication.android.mindall.presentation.dialog.chooseTime

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import myapplication.android.mindall.R
import myapplication.android.mindall.common.Constants.Companion.PAUSED_HOURS
import myapplication.android.mindall.common.Constants.Companion.PAUSED_MINUTES
import myapplication.android.mindall.common.Constants.Companion.PAUSED_SECOND
import myapplication.android.mindall.common.listeners.DialogDismissedListener
import myapplication.android.mindall.databinding.DialogNumberPickerBinding

class ChooseWorkTimeDialogFragment : DialogFragment() {

    private lateinit var listener: DialogDismissedListener
    private var hours = 0
    private var minutes = 0
    private var seconds = 0
    private var isPaused: Boolean = false
    private lateinit var binding: DialogNumberPickerBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireContext())

        binding = DialogNumberPickerBinding.inflate(layoutInflater)

        binding.titleMain.text = getString(R.string.time_to_work)

        initHoursPicker()
        initMinutesPicker()
        initSecondsPicker()

        binding.buttonPause.setOnClickListener {
            binding.buttonPause.setEnabled(false)
            binding.buttonPause.text = ""

            hours = binding.hoursPicker.value
            minutes = binding.minutesPicker.value
            seconds = binding.secondPicker.value
            isPaused = true
            dismiss()
        }

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

        return builder.setView(binding.getRoot()).create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        if (isPaused) {
            val bundle = Bundle()
            bundle.putInt(PAUSED_HOURS, hours)
            bundle.putInt(PAUSED_MINUTES, minutes)
            bundle.putInt(PAUSED_SECOND, seconds)

            listener.handleDialogClose(bundle)
        }
    }

    fun onDismissListener(listener: DialogDismissedListener) {
        this.listener = listener
    }

    private fun initHoursPicker() {
        binding.hoursPicker.setMinValue(0)
        binding.hoursPicker.setMaxValue(99)
        binding.hoursPicker.value = 0
        binding.hoursPicker.setWrapSelectorWheel(false)
    }

    private fun initMinutesPicker() {
        binding.minutesPicker.setMinValue(0)
        binding.minutesPicker.setMaxValue(59)
        binding.minutesPicker.value = 10
        binding.minutesPicker.setWrapSelectorWheel(false)
    }

    private fun initSecondsPicker() {
        binding.secondPicker.setMinValue(0)
        binding.secondPicker.setMaxValue(59)
        binding.secondPicker.value = 0
        binding.secondPicker.setWrapSelectorWheel(false)
    }
}