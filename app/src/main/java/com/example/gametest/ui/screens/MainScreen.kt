package com.example.gametest.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.gametest.R
import com.example.gametest.viewmodel.GameViewModel

/**
 * Écran principal qui contient la navigation entre les différents écrans
 */
@Composable
fun MainScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    
    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                // Onglet "Jeu"
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 0) {
                                Icons.Filled.Home
                            } else {
                                Icons.Outlined.Home
                            },
                            contentDescription = stringResource(R.string.tab_game)
                        )
                    },
                    label = {
                        Text(text = stringResource(R.string.tab_game))
                    }
                )
                
                // Onglet "Boutique"
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 1) {
                                Icons.Filled.ShoppingCart
                            } else {
                                Icons.Outlined.ShoppingCart
                            },
                            contentDescription = stringResource(R.string.tab_shop)
                        )
                    },
                    label = {
                        Text(text = stringResource(R.string.tab_shop))
                    }
                )
                
                // Onglet "Statistiques"
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 2) {
                                Icons.Filled.Timeline
                            } else {
                                Icons.Outlined.Timeline
                            },
                            contentDescription = stringResource(R.string.tab_stats)
                        )
                    },
                    label = {
                        Text(text = stringResource(R.string.tab_stats))
                    }
                )
                
                // Onglet "Réglages"
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 3) {
                                Icons.Filled.Settings
                            } else {
                                Icons.Outlined.Settings
                            },
                            contentDescription = stringResource(R.string.tab_settings)
                        )
                    },
                    label = {
                        Text(text = stringResource(R.string.tab_settings))
                    }
                )
            }
        }
    ) { paddingValues ->
        // Contenu de l'écran en fonction de l'onglet sélectionné
        when (selectedTab) {
            0 -> GameScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(paddingValues)
            )
            1 -> ShopScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(paddingValues)
            )
            2 -> StatsScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(paddingValues)
            )
            3 -> SettingsScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
} 