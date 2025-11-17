package ipca.example.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ipca.example.newsapp.ui.articles.ArticleDetailView
import ipca.example.newsapp.ui.articles.ArticlesListView
import ipca.example.newsapp.ui.HomeScreen
import ipca.example.newsapp.ui.predefinedNewsSources
import ipca.example.newsapp.ui.theme.NewsAppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val currentSourceId = navBackStackEntry?.arguments?.getString("sourceId")

            NewsAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {

                        var title = "News App"

                        if (currentSourceId != null) {
                            val source = predefinedNewsSources.find { it.id == currentSourceId }
                            title = source?.name ?: "Notícias"
                        } else if (currentRoute?.startsWith("article/") == true) {
                            title = "Notícia"
                        }

                        if (currentRoute != "home") {
                            TopAppBar(
                                title = {
                                    Text(text = title)
                                },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        navController.popBackStack()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = "Back"
                                        )
                                    }
                                }
                            )
                        }
                    }

                ) { innerPadding ->
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = "home"
                    ){
                        composable("home"){
                            HomeScreen(navController = navController)
                        }
                        composable("articles_list/{sourceId}"){ backStackEntry ->
                            val source = backStackEntry.arguments?.getString("sourceId") ?: ""
                            ArticlesListView(
                                navController = navController,
                                source = source
                            )
                        }
                        composable("article/{url}"){
                            val url = it.arguments?.getString("url")?:""
                            ArticleDetailView(
                                url = url,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}