package myapplication.android.mindall.presentation.plans.trackers.moodTrackers

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import myapplication.android.mindall.R
import myapplication.android.mindall.databinding.FragmentMoodTrackersBinding

class MoodTrackersFragment : Fragment() {
    private lateinit var binding: FragmentMoodTrackersBinding
    private val viewModel: MoodTrackersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoodTrackersBinding.inflate(layoutInflater)
        return binding.root
    }
}