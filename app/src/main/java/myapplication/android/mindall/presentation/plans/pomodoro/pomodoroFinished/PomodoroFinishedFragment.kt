package myapplication.android.mindall.presentation.plans.pomodoro.pomodoroFinished

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import myapplication.android.mindall.R
import myapplication.android.mindall.databinding.FragmentChooseTimeBinding
import myapplication.android.mindall.databinding.FragmentPomodoroFinishedBinding


class PomodoroFinishedFragment : Fragment() {

    private lateinit var binding: FragmentPomodoroFinishedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPomodoroFinishedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButton()
        initText()
    }

    private fun initText() {
        binding.layoutBox.body.text = getString(R.string.come_back_any_time)
    }

    private fun initButton() {
        binding.buttonGreat.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_pomodoroFinishedFragment_to_chooseTimeFragment)
        }
    }

}