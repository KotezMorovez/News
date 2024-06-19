package com.example.news.data.mapper

import com.example.news.data.model.news_api.SourcesListEntity
import com.example.news.data.model.news_api.SourceEntity
import com.example.news.domain.model.profile.Source
import com.example.news.domain.model.profile.SourcesList

fun SourcesListEntity.toDomain(): SourcesList {
    return SourcesList(
        sources = this.sources.map { it.toDomain() }
    )
}

fun SourceEntity.toDomain(): Source {
    return Source(
        id = this.id,
        name = this.name,
        language = this.language
    )
}