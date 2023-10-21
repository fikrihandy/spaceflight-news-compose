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

// Spaceflight News API: https://api.spaceflightnewsapi.net/v4/docs/