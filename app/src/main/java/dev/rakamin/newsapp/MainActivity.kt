package dev.rakamin.newsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.rakamin.newsapp.adapter.NewsAdapter
import dev.rakamin.newsapp.model.Article
import dev.rakamin.newsapp.network.NewsApiService
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var newsApiService: NewsApiService
    private lateinit var newsAdapter: NewsAdapter

    private var currentPage = 1
    private val pageSize = 20
    private var isLastPage = false
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi objek NewsApiService menggunakan Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        newsApiService = retrofit.create(NewsApiService::class.java)

        // Inisialisasi objek NewsAdapter
        newsAdapter = NewsAdapter()

        // Set LayoutManager dan Adapter pada RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = newsAdapter

        // Implementasi Endless/Infinite Scrolling
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val visibleItemCount = recyclerView.layoutManager?.childCount ?: 0
                    val totalItemCount = recyclerView.layoutManager?.itemCount ?: 0
                    val firstVisibleItemPosition =
                        (recyclerView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()
                            ?: 0

                    if (!isLoading && !isLastPage) {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                            loadMoreNews()
                        }
                    }
                }
            }
        })

        // Mengambil data berita pertama kali
        fetchNews()
    }

    private fun fetchNews() {
        isLoading = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = newsApiService.getAllNews(API_KEY, currentPage, pageSize)
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    val news: List<Article> = newsResponse

                    withContext(Dispatchers.Main) {
                        if (news.isNotEmpty()) {
                            newsAdapter.addNews(news)
                            currentPage++
                        } else {
                            isLastPage = true
                        }
                    }
                } else {
                    val errorMessage = response.message()
                    withContext(Dispatchers.Main) {
                        handleErrorResponse(errorMessage)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    handleNetworkFailure()
                }
            } finally {
                isLoading = false
            }
        }
    }

    private fun loadMoreNews() {
        // Load more news with incremented page number
        fetchNews()
    }

    private fun handleNetworkFailure() {
        // Handle network failure here
    }

    private fun handleErrorResponse(errorMessage: String?) {
        // Handle error response here

    }

    companion object {
        private const val API_KEY = "6c7e12d410ca4d1897335e5b296b437f"
    }
}
