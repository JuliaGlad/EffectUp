package myapplication.android.mindall.presentation.profile.login

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import myapplication.android.mindall.MainActivity
import myapplication.android.mindall.R
import myapplication.android.mindall.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLauncher()
    }

    private fun initLauncher() {
        launcher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                NavHostFragment.findNavController(this)
                    .navigate(R.id.action_loginFragment_to_navigation_profile)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserves()
        initCreateAccountAction()
        initLoginButton()
    }

    private fun initLoginButton() {
        binding.loginButton.setOnClickListener {
            val email = binding.editLayoutEmail.text.toString()
            val password = binding.editLayoutPassword.text.toString()
            viewModel.signIn(email, password)
        }
    }

    private fun setupObserves() {
        viewModel.isLogged.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it) {
                    NavHostFragment.findNavController(this)
                        .navigate(R.id.action_loginFragment_to_navigation_profile)
                }
            }
        }
    }

    private fun initCreateAccountAction() {
        binding.createActionText.setOnClickListener {
            (activity as MainActivity).openCreateAccountActivity(launcher)
        }
    }
}