package academy.bangkit.spaceflightnews.ui.screen.home

import academy.bangkit.spaceflightnews.Loading
import academy.bangkit.spaceflightnews.R
import academy.bangkit.spaceflightnews.data.repository.ArticleRepository
import academy.bangkit.spaceflightnews.ui.navigation.Screen
import academy.bangkit.spaceflightnews.ui.screen.ViewModelFactory
import academy.bangkit.spaceflightnews.ui.theme.SpaceflightNewsTheme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(factory = ViewModelFactory(ArticleRepository())),
    navController: NavHostController
) {

    Box(modifier = Modifier.fillMaxSize())
    {
        val scope = rememberCoroutineScope()
        val articles by viewModel.articles.observeAsState(emptyList())
        val isLoading by viewModel.isLoading.observeAsState(initial = false)
        val listState = rememberLazyListState()
        val showButton: Boolean by remember {
            derivedStateOf {
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == articles.lastIndex
            }
        }
        var snackBarVisible by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            if (articles.isEmpty()) {
                viewModel.getArticles()
                snackBarVisible = true
                delay(5000)
                snackBarVisible = false
            }
        }

        if (snackBarVisible && viewModel.count > 0 && articles.isNotEmpty() && !isLoading) {
            Snackbar(
                modifier = Modifier
                    .padding(16.dp)
                    .zIndex(1f)
                    .align(Alignment.BottomCenter),
                action = {
                    TextButton(onClick = {
                        snackBarVisible = false
                    }) {
                        Text(text = "Dismiss")
                    }
                }
            ) {
                Text(viewModel.snackBarMessage)
            }
        }

        Loading(isVisible = isLoading)
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
                        .animateItemPlacement(tween(durationMillis = 100)),
                    onClick = {
                        navController.navigate(Screen.Detail.createRoute(article.id)) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
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
                isLoading = isLoading,
                onClick = {
                    scope.launch {
                        val url = viewModel.nextArticle
                        if (url != null) {
                            val limit = extractQueryParam(url, "limit")
                            val offset = extractQueryParam(url, "offset")
                            if (limit != null && offset != null) {
                                viewModel.loadMoreArticles(limit, offset).run {
                                    snackBarVisible = true
                                    delay(5000)
                                    snackBarVisible = false
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun ArticleListItem(
    title: String,
    photoUrl: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable { onClick() }
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
private fun LoadMoreArticles(
    onClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = !isLoading
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = stringResource(R.string.load_more),
        )
    }
}

private fun extractQueryParam(url: String, param: String): Int? {
    val regex = Regex("$param=(\\d+)")
    val matchResult = regex.find(url)
    return matchResult?.groupValues?.get(1)?.toIntOrNull()
}

@Preview(showBackground = true)
@Composable
private fun ArticleListItemPreview() {
    SpaceflightNewsTheme {
        ArticleListItem(
            title = "Title of Article",
            photoUrl = "https://contenthub-static.grammarly.com/blog/wp-content/uploads/2022/08/BMD-3398.png",
            onClick = {}
        )
    }
}