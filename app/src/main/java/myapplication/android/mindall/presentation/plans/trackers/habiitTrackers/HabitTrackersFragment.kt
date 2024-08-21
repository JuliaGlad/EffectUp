package myapplication.android.mindall.presentation.plans.trackers.habiitTrackers

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import myapplication.android.mindall.R
import myapplication.android.mindall.databinding.FragmentHabitTrackersBinding

class HabitTrackersFragment : Fragment() {

    private lateinit var binding: FragmentHabitTrackersBinding
    private val viewModel: HabitTrackersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHabitTrackersBinding.inflate(layoutInflater)
        return binding.root
    }
}