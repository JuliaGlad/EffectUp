package myapplication.android.mindall.presentation.dialog.alreadyHaveTrackerDialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import myapplication.android.mindall.databinding.DialogAlreadyHaveTrackerBinding

class AlreadyHaveTrackerDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogAlreadyHaveTrackerBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(requireContext())

        binding.buttonOk.setOnClickListener {dismiss()}

        return builder.setView(binding.root).create()
    }
}