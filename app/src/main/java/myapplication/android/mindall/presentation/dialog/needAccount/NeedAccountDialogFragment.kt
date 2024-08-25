package myapplication.android.mindall.presentation.dialog.needAccount

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import myapplication.android.mindall.databinding.DialogAlreadyHaveTrackerBinding
import myapplication.android.mindall.databinding.DialogNeedAccountBinding

class NeedAccountDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogNeedAccountBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(requireContext())

        binding.buttonOk.setOnClickListener {dismiss()}

        return builder.setView(binding.root).create()
    }
}