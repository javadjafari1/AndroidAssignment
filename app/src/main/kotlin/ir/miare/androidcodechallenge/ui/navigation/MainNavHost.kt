package ir.miare.androidcodechallenge.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ir.miare.androidcodechallenge.ui.screens.followed.FollowedListScreen
import ir.miare.androidcodechallenge.ui.screens.home.HomeScreen

internal fun NavGraphBuilder.mainNavHost(
    navController: NavHostController,
) {
    animatedComposable<AppScreens.Home> {
        HomeScreen(navController)
    }

    animatedComposable<AppScreens.FollowedList> {
        FollowedListScreen(navController)
    }
}
