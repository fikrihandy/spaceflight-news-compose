package academy.bangkit.spaceflightnews.data.repository

import academy.bangkit.spaceflightnews.data.response.Article
import academy.bangkit.spaceflightnews.data.response.GetArticlesResponse
import academy.bangkit.spaceflightnews.data.retrofit.ApiConfig
import android.util.Log

class ArticleRepository {
    suspend fun getArticles(): GetArticlesResponse {
        return ApiConfig.getApiService().getArticles()
    }

    suspend fun getArticles(limit: Int, offset: Int): GetArticlesResponse {
        return ApiConfig.getApiService().getArticles(limit = limit, offset = offset)
    }

    suspend fun getArticle(id: Int): Article {
        Log.d("article", "getArticle in repository")
        return ApiConfig.getApiService().getDetailArticle(detail = id)
    }
}