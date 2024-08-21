package myapplication.android.mindall.presentation.profile.createAccount.main

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import myapplication.android.mindall.R
import myapplication.android.mindall.databinding.FragmentCreateAccountBinding

class CreateAccountFragment : Fragment() {

    private lateinit var binding: FragmentCreateAccountBinding
    private val viewModel: CreateAccountViewModel by viewModels()

    override fun onStart() {
        super.onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserves()
        setupTextWatchers()
        initCreateButton()
        initBackButton()
    }

    private fun initBackButton() {
        binding.buttonBack.setOnClickListener {
            activity?.finish()
        }
    }

    private fun setupTextWatchers() {
        binding.editLayoutPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.removePasswordError()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.editLayoutEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.removeEmailError()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.editLayoutNamer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.removeNameError()
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    private fun setupObserves() {

        viewModel.isCreated.observe(viewLifecycleOwner) {
            it.let {
                if (it){
                    activity?.apply {
                        setResult(RESULT_OK)
                        finish()
                    }
                }
            }
        }

        viewModel.sendPasswordError.observe(viewLifecycleOwner) {
            it.let {
                binding.textLayoutPassword.error = it
            }
        }

        viewModel.sendNameError.observe(viewLifecycleOwner) {
            it.let {
                binding.textLayoutName.error = it
            }
        }

        viewModel.sendPhoneError.observe(viewLifecycleOwner) {
            it.let {
                binding.textLayoutEmail.error = it
            }
        }
    }

    private fun initCreateButton() {
        binding.loginButton.setOnClickListener {
            val name = binding.editLayoutNamer.text.toString()
            val password = binding.editLayoutPassword.text.toString()
            val email = binding.editLayoutEmail.text.toString()

            val check = checkData(password, name, email)
            if (check) {
                viewModel.createAccountWithPhoneNumberAndPassword(name, email, password)
            }
        }
    }

    private fun checkData(
        password: String,
        name: String,
        email: String
    ): Boolean {

        var check = true

        if (password.isEmpty()) {
            viewModel.sendPasswordError(getString(R.string.this_field_is_required))
            check = false
        }

        if (name.isEmpty()) {
            viewModel.sendNameError(getString(R.string.this_field_is_required))
            check = false
        }

        if (email.isEmpty()) {
            viewModel.sendEmailError(getString(R.string.this_field_is_required))
            check = false
        }

        return check
    }

}