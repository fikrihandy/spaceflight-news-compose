package academy.bangkit.spaceflightnews

import academy.bangkit.spaceflightnews.ui.navigation.Screen
import academy.bangkit.spaceflightnews.ui.screen.detail.DetailScreen
import academy.bangkit.spaceflightnews.ui.screen.home.HomeScreen
import academy.bangkit.spaceflightnews.ui.screen.profile.ProfileScreen
import academy.bangkit.spaceflightnews.ui.theme.SpaceflightNewsTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpaceflightNewsApp(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.Profile.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "About Me"
                        )
                    }
                }
            )
        }
    )
    { innerPadding ->
        NavigationHost(navController, innerPadding)
    }
}

@Composable
fun NavigationHost(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(
            Screen.Detail.route,
            arguments = listOf(
                navArgument("articleId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val articleId = backStackEntry.arguments?.getInt("articleId") ?: 0
            DetailScreen(articleId = articleId)
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}

@Composable
fun OpenUrl(title: String, url: String, modifier: Modifier) {
    val uriHandler = LocalUriHandler.current
    Button(content = {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
        )
    }, onClick = {
        uriHandler.openUri(url)
    }, modifier = modifier)
}

@Composable
fun Loading(isVisible: Boolean) {
    if (isVisible) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SpaceflightNewsAppPreview() {
    SpaceflightNewsTheme {
        SpaceflightNewsApp()
    }
}