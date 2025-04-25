package com.example.gametest.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gametest.R
import com.example.gametest.model.Upgrade
import com.example.gametest.ui.components.UpgradeItem
import com.example.gametest.viewmodel.GameViewModel

/**
 * Écran de la boutique pour acheter des améliorations
 */
@Composable
fun ShopScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val unlockableUpgrades by remember {
        derivedStateOf {
            viewModel.gameState.upgrades.filter { it.unlocked || shouldUnlock(it, viewModel.gameState.cookies) }
        }
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Affichage du nombre de cookies
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text(
                text = stringResource(R.string.cookie_count, viewModel.formatNumber(viewModel.gameState.cookies)),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Liste des améliorations
        if (unlockableUpgrades.isEmpty()) {
            Text(
                text = "Aucune amélioration disponible pour le moment.\nContinuez à cliquer pour débloquer des améliorations!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(32.dp)
            )
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth()
            ) {
                items(
                    items = unlockableUpgrades,
                    key = { upgrade -> upgrade.id }
                ) { upgrade ->
                    val canBuy = viewModel.canBuyUpgrade(upgrade)
                    val formattedCost = viewModel.formatNumber(upgrade.currentCost())
                    val formattedProduction = viewModel.formatNumberWithDecimal(upgrade.currentProduction())
                    
                    UpgradeItem(
                        upgrade = upgrade,
                        formattedCost = formattedCost,
                        formattedProduction = formattedProduction,
                        canBuy = canBuy,
                        onBuyClick = {
                            viewModel.buyUpgrade(upgrade)
                        }
                    )
                }
            }
        }
    }
}

/**
 * Détermine si une amélioration devrait être déverrouillée
 */
private fun shouldUnlock(upgrade: Upgrade, cookies: Long): Boolean {
    // Si le joueur a au moins 1/3 du coût de base, déverrouiller l'amélioration
    return cookies >= upgrade.baseCost / 3
} 