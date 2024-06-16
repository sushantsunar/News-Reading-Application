package com.ss.newsapp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.ss.newsapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        fetchData()
       mAdapter = NewsListAdapter( this)



        binding.recyclerView.adapter = mAdapter


        }

    private fun fetchData() {
        val url =
            "https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=c00f4f336b8a44c692dd15cf894a9648"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {
              val newJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newJsonArray.length()){
                    val newJsonObject = newJsonArray.getJSONObject(i)
                    val news = News(
                        newJsonObject.getString("title"),
                        newJsonObject.getString("author"),
                        newJsonObject.getString("url"),
                        newJsonObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
                runOnUiThread { // Update adapter on the main thread
                    mAdapter.updateNews(newsArray)
                }
            },
            {
                    error ->
                Log.e("fetchData", "Error fetching news: ${error.message}")
                // Optionally, display an error message to the user

            }

        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }


    override fun onItemClicked(item: News) {
        val url = "https://developers.android.com"
        val builder = CustomTabsIntent.Builder()
          val customTabsIntent =   builder.build()
       customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
}
