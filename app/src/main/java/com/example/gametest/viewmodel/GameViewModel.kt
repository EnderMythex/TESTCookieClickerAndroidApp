package com.example.gametest.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gametest.data.GameDataManager
import com.example.gametest.model.GameState
import com.example.gametest.model.Upgrade
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * ViewModel pour gérer la logique du jeu Cookie Clicker
 */
class GameViewModel(application: Application) : AndroidViewModel(application) {
    
    // État du jeu
    val gameState = GameState()
    
    // Gestionnaire des données du jeu
    private val gameDataManager = GameDataManager(application.applicationContext)
    
    // Job pour la production passive de cookies
    private var productionJob: Job? = null
    
    // Initialisation
    init {
        // Charger les données sauvegardées
        viewModelScope.launch {
            loadGame()
            startProduction()
        }
    }
    
    /**
     * Commence la production passive de cookies
     */
    private fun startProduction() {
        stopProduction()
        
        productionJob = viewModelScope.launch(Dispatchers.Default) {
            while (isActive) {
                gameState.produceCookies()
                delay(1000) // Production toutes les secondes
            }
        }
    }
    
    /**
     * Arrête la production passive de cookies
     */
    private fun stopProduction() {
        productionJob?.cancel()
        productionJob = null
    }
    
    /**
     * Action de cliquer sur le cookie
     */
    fun clickCookie() {
        gameState.clickCookie()
        
        // Sauvegarde automatique toutes les 10 clics
        if (gameState.totalClicks % 10 == 0L) {
            viewModelScope.launch {
                saveGame()
            }
        }
    }
    
    /**
     * Achat d'une amélioration
     */
    fun buyUpgrade(upgrade: Upgrade): Boolean {
        val success = gameState.buyUpgrade(upgrade.id)
        if (success) {
            viewModelScope.launch {
                saveGame()
            }
        }
        return success
    }
    
    /**
     * Vérifie si une amélioration peut être achetée
     */
    fun canBuyUpgrade(upgrade: Upgrade): Boolean {
        return gameState.cookies >= upgrade.currentCost()
    }
    
    /**
     * Charge la sauvegarde du jeu
     */
    suspend fun loadGame() {
        gameDataManager.loadGameState(gameState)
    }
    
    /**
     * Sauvegarde l'état du jeu
     */
    suspend fun saveGame() {
        gameDataManager.saveGameState(gameState)
    }
    
    /**
     * Réinitialise le jeu
     */
    fun resetGame() {
        viewModelScope.launch {
            gameDataManager.resetGameData()
            gameState.resetGame()
        }
    }
    
    /**
     * Formate un nombre pour l'affichage
     */
    fun formatNumber(number: Long): String {
        return when {
            number < 1_000 -> number.toString()
            number < 1_000_000 -> String.format("%.1fK", number / 1_000.0)
            number < 1_000_000_000 -> String.format("%.1fM", number / 1_000_000.0)
            number < 1_000_000_000_000 -> String.format("%.1fB", number / 1_000_000_000.0)
            else -> String.format("%.1fT", number / 1_000_000_000_000.0)
        }
    }
    
    /**
     * Formate un nombre pour l'affichage avec 1 décimale
     */
    fun formatNumberWithDecimal(number: Double): String {
        return when {
            number < 1_000 -> String.format("%.1f", number)
            number < 1_000_000 -> String.format("%.1fK", number / 1_000.0)
            number < 1_000_000_000 -> String.format("%.1fM", number / 1_000_000.0)
            number < 1_000_000_000_000 -> String.format("%.1fB", number / 1_000_000_000.0)
            else -> String.format("%.1fT", number / 1_000_000_000_000.0)
        }
    }
    
    /**
     * Nettoyage lors de la destruction du ViewModel
     */
    override fun onCleared() {
        super.onCleared()
        stopProduction()
        viewModelScope.launch {
            saveGame()
        }
    }
} 