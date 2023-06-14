package dev.rakamin.newsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dev.rakamin.newsapp.R
import androidx.recyclerview.widget.RecyclerView
import dev.rakamin.newsapp.model.Article
import dev.rakamin.newsapp.model.News

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    private val newsList = mutableListOf<News>()

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentNews = newsList[position]
        holder.titleTextView.text = currentNews.title
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    fun addNews(news: List<Article>) {
        val startPosition = newsList.size
        newsList.addAll(news)
        notifyItemRangeInserted(startPosition, news.size)
    }

}

private fun <E> MutableList<E>.addAll(elements: List<Article>) {
    this.addAll(elements)
}





