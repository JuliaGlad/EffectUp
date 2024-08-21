package myapplication.android.mindall.domain.useCase.dailyPlan

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.di.DI
import myapplication.android.mindall.domain.model.daily.DailyPlanMainModel

class GetDailyPlanByDataUseCase {
        fun invoke(data: String) : Single<DailyPlanMainModel>{
        return DI.dailyTaskRepository.getDailyPlanByData(data).flatMap { dto ->
            Single.just(DailyPlanMainModel(dto.id, dto.data, dto.greenTaskCount, dto.yellowTaskCount, dto.redTaskCount))
        }
    }
}
