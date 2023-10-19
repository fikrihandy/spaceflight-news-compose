package academy.bangkit.spaceflightnews.data.response

import com.google.gson.annotations.SerializedName

data class GetArticlesResponse(

    @field:SerializedName("next")
    val next: String,

    @field:SerializedName("previous")
    val previous: String,

    @field:SerializedName("count")
    val count: Int,

    @field:SerializedName("results")
    val results: List<Article>
)

data class Article(

    @field:SerializedName("summary")
    val summary: String,

    @field:SerializedName("news_site")
    val newsSite: String,

    @field:SerializedName("featured")
    val featured: Boolean,

    @field:SerializedName("updated_at")
    val updatedAt: String,

    @field:SerializedName("image_url")
    val imageUrl: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("published_at")
    val publishedAt: String,

    @field:SerializedName("url")
    val url: String,
)
