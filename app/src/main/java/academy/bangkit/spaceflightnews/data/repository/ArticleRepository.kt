package academy.bangkit.spaceflightnews.data.repository

import academy.bangkit.spaceflightnews.data.response.GetArticlesResponse
import academy.bangkit.spaceflightnews.data.retrofit.ApiConfig

class ArticleRepository {

    suspend fun getArticles(): GetArticlesResponse {
        return ApiConfig.getApiService().getArticles()
    }

    suspend fun getArticles(limit: Int, offset: Int): GetArticlesResponse {
        return ApiConfig.getApiService().getArticles(limit = limit, offset = offset)
    }

}