package ipca.example.cryptoapp.domain.use_case

import ipca.example.cryptoapp.domain.model.Coin
import ipca.example.cryptoapp.domain.repository.CoinRepository

class GetCoinsUseCase(private val repository: CoinRepository) {
    suspend operator fun invoke(): List<Coin> {
        return repository.getCoins()
    }
}