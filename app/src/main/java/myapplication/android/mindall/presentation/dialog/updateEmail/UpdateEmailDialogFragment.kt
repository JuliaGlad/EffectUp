package myapplication.android.mindall.presentation.dialog.updateEmail

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import myapplication.android.mindall.common.Constants.Companion.EMAIL
import myapplication.android.mindall.common.Constants.Companion.PASSWORD
import myapplication.android.mindall.common.listeners.DialogDismissedListener
import myapplication.android.mindall.databinding.DialogUpdateEmailBinding

class UpdateEmailDialogFragment() : DialogFragment()  {

    private val viewModel : UpdateEmailViewModel by viewModels()
    private lateinit var binding: DialogUpdateEmailBinding
    private lateinit var listener: DialogDismissedListener
    private lateinit var email : String
    private lateinit var password : String
    private var isUpdated = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogUpdateEmailBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(requireContext())

        setupObserves()

        binding.buttonCancel.setOnClickListener {dismiss()}

        binding.buttonSend.setOnClickListener{
            email = binding.editLayoutNewEmail.text.toString()
            password = binding.editLayoutPassword.text.toString()
            viewModel.checkPassword(password)
        }

        return builder.setView(binding.getRoot()).create()

    }

    fun onDismissListener(listener: DialogDismissedListener) {
        this.listener = listener
    }

    private fun setupObserves() {
        viewModel.isChecked.observe(this){
            if (it == true) {
                isUpdated = it
                dismiss()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (isUpdated){
            listener.handleDialogClose(Bundle().apply {
                putString(EMAIL, email)
                putString(PASSWORD, password)
            })
        }
    }
}