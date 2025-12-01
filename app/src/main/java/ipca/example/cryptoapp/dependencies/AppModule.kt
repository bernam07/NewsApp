package ipca.example.cryptoapp.di

import ipca.example.cryptoapp.data.repository.CoinRepositoryImplementation
import ipca.example.cryptoapp.domain.repository.CoinRepository
import ipca.example.cryptoapp.domain.use_case.GetCoinsUseCase

object AppModule {

    private val coinRepository: CoinRepository by lazy {
        CoinRepositoryImplementation()
    }

    val getCoinsUseCase: GetCoinsUseCase by lazy {
        GetCoinsUseCase(coinRepository)
    }
}