package myapplication.android.mindall.presentation.dialog.chooseTimeDialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import myapplication.android.mindall.common.Constants.Companion.HOURS
import myapplication.android.mindall.common.Constants.Companion.MINUTES
import myapplication.android.mindall.common.Constants.Companion.TIME
import myapplication.android.mindall.common.listeners.DialogDismissedListener
import myapplication.android.mindall.databinding.DialogChooseTimeBinding

class ChooseTimeDialogFragment : DialogFragment() {

    private lateinit var binding : DialogChooseTimeBinding
    private var time : String? = null
    private var hours: String? = null
    private var minutes: String? = null
    private lateinit var listener: DialogDismissedListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogChooseTimeBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(requireContext())

        binding.buttonOk.setOnClickListener {
            val hours: String = binding.timepicker.hour.toString()
            val minutes: String = binding.timepicker.minute.toString()
            time = "$hours:$minutes"
            this.hours = hours
            this.minutes = minutes
            dismiss()
        }

        return builder.setView(binding.root).create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (time != null && hours != null && minutes != null){
            listener.handleDialogClose(Bundle().apply{
                putString(TIME, time)
                putString(HOURS, hours)
                putString(MINUTES, minutes)
            })
        }
    }

    fun onDismissListener(listener: DialogDismissedListener) {
        this.listener = listener
    }
}