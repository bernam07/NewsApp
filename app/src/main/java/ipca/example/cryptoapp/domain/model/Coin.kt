package ipca.example.cryptoapp.domain.model

data class Coin(
    val id: String,
    val name: String,
    val symbol: String,
    val rank: Int,
    val priceUsd: Double,
    val changePercent24h: Double,
    val supply: Double,
    val maxSupply: Double?,
    val marketCapUsd: Double?,
    val volume24h: Double?,
    val imageUrl: String
)