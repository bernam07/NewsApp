package ipca.example.cryptoapp.ui.coins

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ipca.example.cryptoapp.di.AppModule
import ipca.example.cryptoapp.domain.model.Coin
import kotlinx.coroutines.launch

data class CoinsListState(
    val coins: List<Coin> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class CoinsListViewModel : ViewModel() {

    var uiState = mutableStateOf(CoinsListState())
        private set

    private val getCoinsUseCase = AppModule.getCoinsUseCase

    init {
        fetchCoins()
    }

    fun fetchCoins() {
        uiState.value = uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val coins = getCoinsUseCase()
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    coins = coins
                )
            } catch (e: Exception) {
                e.printStackTrace()
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Erro desconhecido"
                )
            }
        }
    }
}