package com.example.gametest.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.gametest.R
import com.example.gametest.model.Upgrade

/**
 * Composant pour afficher un élément d'amélioration dans la boutique
 */
@Composable
fun UpgradeItem(
    upgrade: Upgrade,
    formattedCost: String,
    formattedProduction: String,
    canBuy: Boolean,
    onBuyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    val backgroundColor by animateColorAsState(
        targetValue = if (canBuy) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        animationSpec = tween(300),
        label = "BackgroundColor"
    )
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Icône de l'amélioration
                val iconRes = when (upgrade.id) {
                    1 -> R.drawable.cursor_upgrade
                    2 -> R.drawable.grandma_upgrade
                    3 -> R.drawable.farm_upgrade
                    else -> R.drawable.cursor_upgrade // Icône par défaut
                }
                
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = upgrade.name,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(8.dp)
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Informations de l'amélioration
                Column {
                    Text(
                        text = upgrade.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = stringResource(R.string.upgrade_level, upgrade.level),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Text(
                        text = stringResource(R.string.upgrade_cost, formattedCost),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (canBuy) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            Color.Gray
                        }
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Bouton d'achat
                Button(
                    onClick = onBuyClick,
                    enabled = canBuy,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(text = stringResource(R.string.buy_upgrade))
                }
            }
            
            // Description détaillée (visible uniquement si expanded est true)
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = upgrade.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = if (expanded) Int.MAX_VALUE else 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = stringResource(R.string.upgrade_production, formattedProduction),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
} 