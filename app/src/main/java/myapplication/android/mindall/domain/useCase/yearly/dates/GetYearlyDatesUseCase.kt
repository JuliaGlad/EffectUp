package myapplication.android.mindall.domain.useCase.yearly.dates

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.di.DI
import myapplication.android.mindall.domain.model.yearly.ImportantDateModel
import java.util.stream.Collectors

class GetYearlyDatesUseCase {
    fun invoke(yearId: String): Single<List<ImportantDateModel>> {
        return DI.yearlyDats.getDates(yearId).map { dtos ->
            dtos.stream()
                .map { dto -> ImportantDateModel(dto.id, dto.date, dto.event) }
                .collect(Collectors.toList())
        }
    }
}