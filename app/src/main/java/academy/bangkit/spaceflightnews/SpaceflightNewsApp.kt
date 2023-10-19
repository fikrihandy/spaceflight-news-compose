package academy.bangkit.spaceflightnews

import academy.bangkit.spaceflightnews.data.repository.ArticleRepository
import academy.bangkit.spaceflightnews.ui.theme.SpaceflightNewsTheme
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SpaceflightNewsApp(
    viewModel: SpaceflightNewsViewModel = viewModel(factory = ViewModelFactory(ArticleRepository()))
) {

    val articles by viewModel.articles.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(initial = false)

    LaunchedEffect(Unit) {
        viewModel.getArticles()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "About Me"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            val scope = rememberCoroutineScope()
            val listState = rememberLazyListState()
            val showButton: Boolean by remember {
                derivedStateOf {
                    listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == articles.lastIndex
                }
            }

            LinearProgressExample(isVisible = isLoading)

            LazyColumn(
                state = listState,
                modifier = Modifier.padding(top = 8.dp, end = 16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(articles, key = { it.id }) { article ->
                    ArticleListItem(
                        title = article.title,
                        photoUrl = article.imageUrl,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement(tween(durationMillis = 100))
                    )
                }
            }

            AnimatedVisibility(
                visible = showButton,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically(),
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .align(Alignment.BottomCenter)
            ) {
                LoadMoreArticles(
                    onClick = {
                        scope.launch {
                            val url = viewModel.nextArticle
                            if (url != null) {
                                val limit = extractQueryParam(url, "limit")
                                val offset = extractQueryParam(url, "offset")
                                if (limit != null && offset != null) {
                                    viewModel.loadMoreArticles(limit, offset)
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ArticleListItem(
    title: String,
    photoUrl: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable {}
    ) {
        AsyncImage(
            model = photoUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
        )
        Text(
            text = title,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 16.dp, end = 8.dp, top = 8.dp)
                .align(Alignment.Top)
        )
    }
}

@Composable
fun LinearProgressExample(isVisible: Boolean) {
    if (isVisible) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun LoadMoreArticles(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = stringResource(R.string.load_more),
        )
    }
}

fun extractQueryParam(url: String, param: String): Int? {
    val regex = Regex("$param=(\\d+)")
    val matchResult = regex.find(url)
    return matchResult?.groupValues?.get(1)?.toIntOrNull()
}

@Preview(showBackground = true)
@Composable
fun JetHeroesAppPreview() {
    SpaceflightNewsTheme {
        SpaceflightNewsApp()
    }
}

@Preview(showBackground = true)
@Composable
fun HeroListItemPreview() {
    SpaceflightNewsTheme {
        ArticleListItem(
            title = "H.O.S. Cokroaminoto",
            photoUrl = ""
        )
    }
}

