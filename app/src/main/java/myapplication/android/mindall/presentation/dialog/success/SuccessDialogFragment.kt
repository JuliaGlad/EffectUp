package myapplication.android.mindall.presentation.dialog.success

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import myapplication.android.mindall.databinding.DialogSuccessfullyUpdatedBinding

class SuccessDialogFragment : DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogSuccessfullyUpdatedBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(requireContext())

        binding.buttonOk.setOnClickListener {dismiss()}

        return builder.setView(binding.getRoot()).create()
    }
}