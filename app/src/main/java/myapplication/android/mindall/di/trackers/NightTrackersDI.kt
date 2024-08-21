package myapplication.android.mindall.di.trackers

import myapplication.android.mindall.domain.useCase.nightTrackers.AddNightTrackerUseCase
import myapplication.android.mindall.domain.useCase.nightTrackers.GetMonthIdUseCase
import myapplication.android.mindall.domain.useCase.nightTrackers.GetNightTrackersUseCase
import myapplication.android.mindall.domain.useCase.nightTrackers.GetYearIdUseCase

class NightTrackersDI {
    companion object{
        val getYearIdUseCase = GetYearIdUseCase()
        val getMonthIdUseCase = GetMonthIdUseCase()
        val getNightTrackersUseCase = GetNightTrackersUseCase()
        val addNightTrackerUseCase = AddNightTrackerUseCase()
    }
}