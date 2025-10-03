package ir.miare.androidcodechallenge.ui.navigation

import kotlinx.serialization.Serializable

internal sealed interface AppScreens {

    @Serializable
    data object Home : AppScreens

    @Serializable
    data object FollowedList : AppScreens
}
