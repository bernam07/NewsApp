package ipca.example.newsapp.data

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import ipca.example.newsapp.models.Article
import ipca.example.newsapp.BuildConfig
import org.json.JSONObject

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

    fun getArticles(source: String, completion: (List<Article>) -> Unit) {
        val apiKey = BuildConfig.API_KEY
        val url = "https://newsapi.org/v2/top-headlines?sources=$source&apiKey=$apiKey"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val articlesJson = response.getJSONArray("articles")
                val articlesList = mutableListOf<Article>()
                for (i in 0 until articlesJson.length()) {
                    val articleJson = articlesJson.getJSONObject(i)
                    articlesList.add(Article.fromJson(articleJson))
                }
                completion(articlesList)
            },
            { error ->
                error.printStackTrace()
                completion(emptyList())
            }
        )
        requestQueue.add(request)
    }
}