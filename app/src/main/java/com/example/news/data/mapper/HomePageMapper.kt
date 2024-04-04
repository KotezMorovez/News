package com.example.news.data.mapper

import android.util.Log
import com.example.news.data.model.news_api.ArticleEntity
import com.example.news.data.model.news_api.NewsEntity
import com.example.news.domain.model.home_page.response.Article
import com.example.news.domain.model.home_page.response.News
import org.joda.time.DateTime
import java.util.UUID

fun NewsEntity.toDomain(): News {
    return News(
        totalResults = this.totalResults,
        articles = this.articles.map {
            it.toDomain()
        }
    )

}

fun ArticleEntity.toDomain(): Article {
    val dateTime = DateTime.parse(this.publishedAt)

    return Article(
        id = UUID.randomUUID().toString(),
        author = this.author,
        title = this.title,
        description = this.description,
        url = this.url,
        imageUrl = this.urlToImage,
        publishedAt = dateTime,
        content = this.content
    )
}