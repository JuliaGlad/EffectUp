package myapplication.android.mindall.domain.useCase.monthly.goals

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.di.DI
import myapplication.android.mindall.domain.model.monthly.GoalModel
import java.util.stream.Collectors

class GetMonthlyGoalsUseCase {
    fun invoke(yearId: String, monthId: String): Single<List<GoalModel>>{
        return DI.monthlyGoals.getMonthlyGoals(yearId, monthId).map { dtos ->
            dtos.stream()
                .map { dto -> GoalModel(dto.id, dto.goal) }
                .collect(Collectors.toList())
        }
    }
}