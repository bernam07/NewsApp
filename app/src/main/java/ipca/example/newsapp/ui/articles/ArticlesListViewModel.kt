package ipca.example.newsapp.ui.articles

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ipca.example.newsapp.models.Article
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

data class ArticlesListState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ArticlesListViewModel : ViewModel() {

    var uiState = mutableStateOf(ArticlesListState())
        private set

    // ⚠️ A TUA API KEY ESTÁ EXPOSTA! Vê a nota no fim da resposta.
    private val API_KEY = "2501a01f4d9941e0975ef80604eff3e3"

    fun fetchArticles(source: String) {
        uiState.value = uiState.value.copy(isLoading = true)

        val request = Request.Builder()
            .url("https://newsapi.org/v2/top-headlines?sources=${source}&apiKey=${API_KEY}")
            .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                viewModelScope.launch {
                    uiState.value = uiState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }

            override fun onResponse(call: Call, response: Response) {
                viewModelScope.launch {
                    response.use {
                        if (!response.isSuccessful) {
                            uiState.value = uiState.value.copy(
                                isLoading = false,
                                error = "Unexpected code $response"
                            )
                            return@launch // Sair do scope
                        }

                        val newsResult = response.body!!.string()
                        val jsonResult = JSONObject(newsResult)
                        val articlesList = arrayListOf<Article>()
                        if (jsonResult.getString("status") == "ok") {
                            val articlesJson = jsonResult.getJSONArray("articles")
                            for (i in 0 until articlesJson.length()) {
                                val articleJson = articlesJson.getJSONObject(i)
                                val article = Article.fromJson(articleJson)
                                articlesList.add(article)
                            }
                        }
                        uiState.value = uiState.value.copy(
                            isLoading = false,
                            articles = articlesList,
                            error = null
                        )
                    }
                }
            }
        })
    }
}