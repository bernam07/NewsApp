package ipca.example.cryptoapp.domain.repository

import ipca.example.cryptoapp.domain.model.Coin

interface CoinRepository {
    suspend fun getCoins(): List<Coin>
}