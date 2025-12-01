package ipca.example.cryptoapp.data.repository

import ipca.example.cryptoapp.domain.model.Coin
import ipca.example.cryptoapp.domain.repository.CoinRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.io.IOException

class CoinRepositoryImplementation : CoinRepository {

    private val client = OkHttpClient()

    override suspend fun getCoins(): List<Coin> {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url("https://api.coinpaprika.com/v1/tickers")
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseBody = response.body?.string() ?: throw IOException("Empty body")
            val jsonArray = JSONArray(responseBody)
            val coinsList = mutableListOf<Coin>()

            val limit = if (jsonArray.length() > 50) 50 else jsonArray.length()

            for (i in 0 until limit) {
                val item = jsonArray.getJSONObject(i)

                // 1. Ler "quotes.USD"
                val quotes = item.optJSONObject("quotes")
                val usd = quotes?.optJSONObject("USD")

                val price = usd?.optDouble("price", 0.0) ?: item.optDouble("price_usd", 0.0)
                val change24h = usd?.optDouble("percent_change_24h", 0.0) ?: item.optDouble("percent_change_24h", 0.0)
                val marketCap = usd?.optDouble("market_cap", 0.0) ?: item.optDouble("market_cap_usd", 0.0)
                val volume24h = usd?.optDouble("volume_24h", 0.0) ?: item.optDouble("volume_24h_usd", 0.0)

                // 2. Supply
                var supply = item.optDouble("circulating_supply", 0.0)
                if (supply == 0.0) supply = item.optDouble("total_supply", 0.0)

                val maxSupply = item.optDouble("max_supply", 0.0)
                val coinId = item.getString("id")

                val coin = Coin(
                    id = coinId,
                    name = item.getString("name"),
                    symbol = item.getString("symbol"),
                    rank = item.getInt("rank"),
                    supply = supply,
                    maxSupply = if (maxSupply == 0.0) null else maxSupply,
                    priceUsd = price,
                    changePercent24h = change24h,
                    marketCapUsd = marketCap,
                    volume24h = volume24h,
                    imageUrl = "https://static.coinpaprika.com/coin/$coinId/logo.png"
                )
                coinsList.add(coin)
            }
            coinsList
        }
    }
}