package academy.bangkit.spaceflightnews.ui.screen.detail

import academy.bangkit.spaceflightnews.Loading
import academy.bangkit.spaceflightnews.OpenUrl
import academy.bangkit.spaceflightnews.R
import academy.bangkit.spaceflightnews.data.repository.ArticleRepository
import academy.bangkit.spaceflightnews.data.response.Article
import academy.bangkit.spaceflightnews.ui.screen.ViewModelFactory
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun DetailScreen(
    viewModel: DetailViewModel = viewModel(factory = ViewModelFactory(ArticleRepository())),
    articleId: Int
) {
    val articleNullable by viewModel.article.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(initial = false)

    Loading(isVisible = isLoading)

    LaunchedEffect(Unit) {
        viewModel.getArticle(articleId)
    }

    if (!isLoading && articleNullable != null) {
        val article = articleNullable as Article
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Divider()
            Text( // Title
                text = article.title,
                style = TextStyle(fontSize = 20.sp),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
            )

            Text( // Time
                text = calculateDateDifference(article.publishedAt),
                style = TextStyle(fontSize = 12.sp, fontStyle = FontStyle.Italic),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 4.dp)
            )

            AsyncImage( // Image
                model = article.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
                    .aspectRatio(1.5f)
            )
            if (article.summary != "") {
                Text( // Summary
                    text = article.summary,
                    modifier = Modifier
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
                )
            }

            Text(
                text = stringResource(R.string.read_full),
                style = TextStyle(fontSize = 12.sp),
                modifier = Modifier
                    .padding(top = 4.dp, start = 16.dp, end = 16.dp, bottom = 4.dp)
                    .align(Alignment.CenterHorizontally)
            )

            OpenUrl(
                article.newsSite,
                article.url,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewDetailScreen(
    viewModel: DetailViewModel = viewModel(factory = ViewModelFactory(ArticleRepository())),
    articleId: Int = 202
) {
    DetailScreen(viewModel, articleId)
}

private fun calculateDateDifference(publishedAt: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    val date = formatter.parse(publishedAt) as Date
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val deviceTime = SimpleDateFormat(pattern, Locale.getDefault())
    val startDate =
        SimpleDateFormat(pattern, Locale.getDefault()).parse(deviceTime.format(date)) as Date
    val endDate =
        SimpleDateFormat(pattern, Locale.getDefault()).parse(deviceTime.format(Date())) as Date
    val diff = endDate.time - startDate.time
    val days = diff / (24 * 60 * 60 * 1000)
    val hours = (diff % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000)
    val minutes = (diff % (60 * 60 * 1000)) / (60 * 1000)
    return when {
        days > 0 -> "$days days $hours hours $minutes minutes ago"
        hours > 0 -> "$hours hours $minutes minutes ago"
        minutes > 0 -> "$minutes minutes ago"
        else -> "less than a minute ago"
    }
}