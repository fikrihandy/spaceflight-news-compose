package academy.bangkit.spaceflightnews.data.retrofit

import academy.bangkit.spaceflightnews.data.response.Article
import academy.bangkit.spaceflightnews.data.response.GetArticlesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("articles")
    suspend fun getArticles(
        @Query("format") format: String = "json",
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): GetArticlesResponse

    @GET("articles")
    suspend fun getArticles(
        @Query("format") format: String = "json"
    ): GetArticlesResponse

    @GET("articles/{articleId}")
    suspend fun getDetailArticle(
        @Path("articleId") detail: Int,
        @Query("format") format: String = "json"
    ): Article
}