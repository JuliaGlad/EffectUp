package myapplication.android.mindall.di.trackers

import myapplication.android.mindall.domain.useCase.moodTracker.AddMoodTrackerUseCase
import myapplication.android.mindall.domain.useCase.moodTracker.GetMonthIdUseCase
import myapplication.android.mindall.domain.useCase.moodTracker.GetMoodTrackersUseCase
import myapplication.android.mindall.domain.useCase.moodTracker.GetYearIdUseCase

class MoodTrackersDI {
    companion object{
        val getYearIdMoodTrackerUseCase = GetYearIdUseCase()
        val getMonthIdUseCase = GetMonthIdUseCase()
        val addMoodTrackerUseCase = AddMoodTrackerUseCase()
        val getMoodTrackerUseCase = GetMoodTrackersUseCase()
    }
}