package dev.rakamin.newsapp.model

import retrofit2.Response

data class NewsResponse(
    val status: String = "",
    val totalResults: Int = 0,
    val articles: List<Article>
)
{
    fun body(): List<Article> {
        return  articles
    }

    fun message(): String? {
        return status
    }

    val isSuccessful: Boolean
        get() = status == "ok"

    companion object {
        val articles: List<Article> = emptyList()
    }
}



data class Article(
    val title: String = "",
    val description: String = "",
    val url: String = ""
)
