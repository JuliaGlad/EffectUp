package myapplication.android.mindall.presentation.dialog.deleteAccount

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import myapplication.android.mindall.common.listeners.DialogDismissedListener
import myapplication.android.mindall.databinding.DialogDeleteAccountBinding

class DeleteAccountDialogFragment: DialogFragment()  {

    private val viewModel : DeleteAccountViewModel by viewModels()
    private var isDeleted = false
    private lateinit var listener: DialogDismissedListener
    private lateinit var binding : DialogDeleteAccountBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogDeleteAccountBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(requireContext())

        setupObserves()

        binding.buttonCancel.setOnClickListener {dismiss()}

        binding.buttonDelete.setOnClickListener{
            val password = binding.editLayoutPassword.text.toString()
            viewModel.deleteAccount(password)
        }

        return builder.setView(binding.getRoot()).create()
    }

    fun onDismissListener(listener: DialogDismissedListener) {
        this.listener = listener
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (isDeleted){
            listener.handleDialogClose(null)
        }
    }

    private fun setupObserves() {
       viewModel.isDeleted.observe(this){
           if (it == true) {
               isDeleted = it
               dismiss()
           }
       }
    }
}