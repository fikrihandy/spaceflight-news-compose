package academy.bangkit.spaceflightnews.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("detail/{articleId}") {
        fun createRoute(articleId: Int) = "detail/$articleId"
    }

    object Profile : Screen("profile")
}
