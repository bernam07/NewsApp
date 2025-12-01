package ipca.example.cryptoapp.ui.coins

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage // Nota: Se usares coil antigo, o import é coil.compose.AsyncImage
import ipca.example.cryptoapp.domain.model.Coin
import ipca.example.cryptoapp.ui.theme.NegativeRed
import ipca.example.cryptoapp.ui.theme.PositiveGreen

@Composable
fun CoinViewCell(
    coin: Coin,
    onClick: () -> Unit
) {
    val isPositive = coin.changePercent24h >= 0
    val changeColor = if (isPositive) PositiveGreen else NegativeRed
    val changeSign = if (isPositive) "+" else ""

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Lado Esquerdo: Imagem + Rank + Nome
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = coin.imageUrl,
                    contentDescription = coin.name,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "${coin.rank}. ${coin.symbol}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = coin.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            // Lado Direito: Preço
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${String.format("%.2f", coin.priceUsd)}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    text = "$changeSign${coin.changePercent24h}%",
                    color = changeColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}