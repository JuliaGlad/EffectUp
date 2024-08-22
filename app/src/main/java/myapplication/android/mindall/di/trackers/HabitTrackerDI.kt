package myapplication.android.mindall.di.trackers

import myapplication.android.mindall.domain.useCase.habitTracker.AddHabitTrackerUseCase
import myapplication.android.mindall.domain.useCase.habitTracker.AddHabitUseCase
import myapplication.android.mindall.domain.useCase.habitTracker.GetHabitIdUseCase
import myapplication.android.mindall.domain.useCase.habitTracker.GetHabitsUseCase
import myapplication.android.mindall.domain.useCase.habitTracker.GetMonthHabitTrackersUseCase
import myapplication.android.mindall.domain.useCase.habitTracker.GetMonthIdUseCase
import myapplication.android.mindall.domain.useCase.habitTracker.GetYearIdUseCase

class HabitTrackerDI {
    companion object{
        val addHabitUseCase = AddHabitUseCase()
        val addHabitTrackerUseCase = AddHabitTrackerUseCase()
        val getHabitIdUseCase = GetHabitIdUseCase()
        val getMonthIdUseCase = GetMonthIdUseCase()
        val getMonthHabitTrackerUseCase = GetMonthHabitTrackersUseCase()
        val getYearIdUseCase = GetYearIdUseCase()
        val getHabitsUseCase = GetHabitsUseCase()
    }
}