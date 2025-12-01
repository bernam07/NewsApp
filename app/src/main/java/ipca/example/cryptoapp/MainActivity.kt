package ipca.example.cryptoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ipca.example.cryptoapp.ui.coins.CoinDetailView
import ipca.example.cryptoapp.ui.coins.CoinsListView
import ipca.example.cryptoapp.ui.coins.CoinsListViewModel
import ipca.example.cryptoapp.ui.theme.CryptoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoAppTheme {
                val navController = rememberNavController()
                val coinsViewModel: CoinsListViewModel = viewModel()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = "coins_list"
                    ) {
                        composable("coins_list") {
                            CoinsListView(
                                navController = navController,
                                viewModel = coinsViewModel
                            )
                        }
                        composable("coin_detail/{coinId}") { backStackEntry ->
                            val coinId = backStackEntry.arguments?.getString("coinId") ?: ""
                            CoinDetailView(
                                viewModel = coinsViewModel,
                                coinId = coinId,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}