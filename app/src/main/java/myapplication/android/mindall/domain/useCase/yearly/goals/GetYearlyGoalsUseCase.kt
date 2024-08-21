package myapplication.android.mindall.domain.useCase.yearly.goals

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.di.DI
import myapplication.android.mindall.domain.model.monthly.GoalModel
import java.util.stream.Collectors

class GetYearlyGoalsUseCase {
    fun invoke(yearId: String): Single<List<GoalModel>>{
        return DI.yearlyGoals.getGoals(yearId).map {dtos ->
            dtos.stream()
                .map { dto -> GoalModel(dto.id, dto.goal) }
                .collect(Collectors.toList())
        }
    }
}