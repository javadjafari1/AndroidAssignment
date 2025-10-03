package ir.miare.androidcodechallenge.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ir.miare.androidcodechallenge.ui.navigation.AppScreens
import ir.miare.androidcodechallenge.ui.navigation.mainNavHost
import ir.miare.androidcodechallenge.ui.theme.AppTheme

internal class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()

                NavHost(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                    navController = navController,
                    startDestination = AppScreens.Home
                ) {
                    mainNavHost(navController)
                }
            }
        }
    }
}
