package ipca.example.newsapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

// Define os itens da barra de navegação
sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object TechCrunch : BottomNavItem("tech_crunch", "TechCrunch", Icons.Default.Info)
    object Bloomberg : BottomNavItem("bloomberg", "Bloomberg", Icons.Default.Info)
    object Espn : BottomNavItem("espn", "ESPN", Icons.Default.Info)
}

@Composable
fun MyBottomBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.TechCrunch,
        BottomNavItem.Bloomberg,
        BottomNavItem.Espn
    )

    NavigationBar {
        // Obtém o estado atual da navegação
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}