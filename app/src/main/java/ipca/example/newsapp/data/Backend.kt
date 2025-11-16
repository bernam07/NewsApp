package ipca.example.newsapp.data

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import ipca.example.newsapp.models.Article
import org.json.JSONObject

private const val API_KEY = "2501a01f4d9941e0975ef80604eff3e3"

class Backend(context: Context) {

    private val requestQueue = Volley.newRequestQueue(context.applicationContext)

    companion object {
        @Volatile
        private var INSTANCE: Backend? = null

        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Backend(context).also { INSTANCE = it }
            }
    }

    // Função para ir buscar os artigos
    fun getArticles(source: String, completion: (List<Article>) -> Unit) {
        val url = "https://newsapi.org/v2/top-headlines?sources=$source&apiKey=$API_KEY"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response -> // Sucesso
                val articlesJson = response.getJSONArray("articles")
                val articlesList = mutableListOf<Article>()
                for (i in 0 until articlesJson.length()) {
                    val articleJson = articlesJson.getJSONObject(i)
                    articlesList.add(Article.fromJson(articleJson))
                }
                // Envia a lista pronta através do callback
                completion(articlesList)
            },
            { error -> // Erro
                error.printStackTrace()
                // Em caso de erro, envia lista vazia
                completion(emptyList())
            }
        )
        requestQueue.add(request)
    }
}