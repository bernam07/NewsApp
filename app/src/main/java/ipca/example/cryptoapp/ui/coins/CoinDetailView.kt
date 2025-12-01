package ipca.example.cryptoapp.ui.coins

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import ipca.example.cryptoapp.ui.theme.NegativeRed
import ipca.example.cryptoapp.ui.theme.PositiveGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailView(
    viewModel: CoinsListViewModel,
    coinId: String,
    navController: NavController
) {
    val coin = viewModel.uiState.value.coins.find { it.id == coinId }

    if (coin == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("A carregar...", color = MaterialTheme.colorScheme.onBackground)
        }
        return
    }

    val isPositive = coin.changePercent24h >= 0
    val color = if (isPositive) PositiveGreen else NegativeRed

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(coin.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // --- CABEÇALHO (Imagem, Símbolo, Preço) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Lado Esquerdo
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = coin.imageUrl,
                        contentDescription = coin.name,
                        modifier = Modifier
                            .size(64.dp) // Aumentei um pouco o logo para destaque
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = coin.symbol,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                // Lado Direito
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$${formatLargeNumber(coin.priceUsd)}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "${if (isPositive) "+" else ""}${String.format("%.2f", coin.changePercent24h)}%",
                        color = color,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(64.dp))

            // --- ESTATÍSTICAS ---
            Text(
                "Estatísticas de Mercado",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(24.dp))

            StatRow(label = "Rank", value = "#${coin.rank}")
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

            StatRow(label = "Market Cap", value = "$${formatLargeNumber(coin.marketCapUsd ?: 0.0)}")
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

            StatRow(label = "Volume (24h)", value = "$${formatLargeNumber(coin.volume24h ?: 0.0)}")
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

            StatRow(label = "Supply em Circulação", value = "${formatLargeNumber(coin.supply)} ${coin.symbol}")
        }
    }
}

@Composable
fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        Text(
            value,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp
        )
    }
}

fun formatLargeNumber(number: Double): String {
    return when {
        number >= 1_000_000_000_000 -> String.format("%.2f T", number / 1_000_000_000_000)
        number >= 1_000_000_000 -> String.format("%.2f B", number / 1_000_000_000)
        number >= 1_000_000 -> String.format("%.2f M", number / 1_000_000)
        else -> String.format("%.2f", number)
    }
}