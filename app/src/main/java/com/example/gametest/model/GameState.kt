package com.example.gametest.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Classe qui représente une amélioration dans le jeu
 */
data class Upgrade(
    val id: Int,
    val name: String,
    val description: String,
    val baseCost: Long,
    val baseProduction: Double,
    val imageResId: Int? = null,
    var level: Int = 0,
    var unlocked: Boolean = false
) {
    fun currentCost(): Long = (baseCost * Math.pow(1.15, level.toDouble())).toLong()
    fun currentProduction(): Double = baseProduction * level
}

/**
 * Classe qui gère l'état du jeu Cookie Clicker
 */
class GameState {
    // Nombre de cookies
    var cookies by mutableStateOf(0L)
        private set
    
    // Cookies par clic
    var cookiesPerClick by mutableStateOf(1L)
        private set
    
    // Cookies par seconde (production passive)
    var cookiesPerSecond by mutableStateOf(0.0)
        private set
    
    // Total des cookies gagnés (statistique)
    var totalCookiesEarned by mutableStateOf(0L)
        private set
    
    // Nombre total de clics (statistique)
    var totalClicks by mutableStateOf(0L)
        private set
    
    // Liste des améliorations disponibles
    var upgrades by mutableStateOf<List<Upgrade>>(emptyList())
        private set
    
    // Multiplicateur global
    var globalMultiplier by mutableStateOf(1.0)
        private set
    
    // Initialisation du jeu
    init {
        resetGame()
    }
    
    // Méthode pour cliquer sur le cookie
    fun clickCookie() {
        addCookies(cookiesPerClick)
        totalClicks++
    }
    
    // Méthode pour ajouter des cookies
    fun addCookies(amount: Long) {
        cookies += amount
        totalCookiesEarned += amount
    }
    
    // Méthode pour produire des cookies passivement (appelée chaque seconde)
    fun produceCookies() {
        val production = cookiesPerSecond
        if (production > 0) {
            addCookies(production.toLong())
        }
    }
    
    // Méthode pour acheter une amélioration
    fun buyUpgrade(upgradeId: Int): Boolean {
        val upgrade = upgrades.find { it.id == upgradeId } ?: return false
        val cost = upgrade.currentCost()
        
        if (cookies >= cost) {
            cookies -= cost
            upgrade.level++
            recalculateProduction()
            return true
        }
        return false
    }
    
    // Recalculer la production par seconde
    private fun recalculateProduction() {
        var newCps = 0.0
        for (upgrade in upgrades) {
            newCps += upgrade.currentProduction()
        }
        cookiesPerSecond = newCps * globalMultiplier
    }
    
    // Méthode pour augmenter le multiplicateur global
    fun increaseMultiplier(amount: Double) {
        globalMultiplier += amount
        recalculateProduction()
    }
    
    // Méthode pour augmenter les cookies par clic
    fun increaseCookiesPerClick(amount: Long) {
        cookiesPerClick += amount
    }
    
    // Déverrouiller une amélioration
    fun unlockUpgrade(upgradeId: Int) {
        upgrades.find { it.id == upgradeId }?.let {
            it.unlocked = true
        }
    }
    
    // Réinitialiser le jeu
    fun resetGame() {
        cookies = 0
        cookiesPerClick = 1
        cookiesPerSecond = 0.0
        totalCookiesEarned = 0
        totalClicks = 0
        globalMultiplier = 1.0
        
        // Création des améliorations
        upgrades = listOf(
            Upgrade(
                id = 1,
                name = "Curseur automatique",
                description = "Clique automatiquement une fois par seconde",
                baseCost = 15,
                baseProduction = 0.1,
                unlocked = true
            ),
            Upgrade(
                id = 2,
                name = "Grand-mère",
                description = "Une grand-mère gentille qui fait des cookies",
                baseCost = 100,
                baseProduction = 1.0,
                unlocked = true
            ),
            Upgrade(
                id = 3,
                name = "Ferme",
                description = "Cultive des cookies frais",
                baseCost = 1100,
                baseProduction = 8.0,
                unlocked = true
            ),
            Upgrade(
                id = 4,
                name = "Mine",
                description = "Extrait des pépites de chocolat",
                baseCost = 12000,
                baseProduction = 47.0
            ),
            Upgrade(
                id = 5,
                name = "Usine",
                description = "Produit des cookies en masse",
                baseCost = 130000,
                baseProduction = 260.0
            ),
            Upgrade(
                id = 6,
                name = "Banque",
                description = "Génère des cookies à partir d'intérêts financiers",
                baseCost = 1400000,
                baseProduction = 1400.0
            ),
            Upgrade(
                id = 7,
                name = "Temple",
                description = "Rempli de prêtres adeptes du cookie",
                baseCost = 20000000,
                baseProduction = 7800.0
            ),
            Upgrade(
                id = 8,
                name = "Tour de sorcier",
                description = "Conjure des cookies par magie",
                baseCost = 330000000,
                baseProduction = 44000.0
            ),
            Upgrade(
                id = 9,
                name = "Vaisseau spatial",
                description = "Apporte des cookies d'autres planètes",
                baseCost = 5100000000,
                baseProduction = 260000.0
            ),
            Upgrade(
                id = 10,
                name = "Laboratoire d'alchimie",
                description = "Transforme tout en cookies",
                baseCost = 75000000000,
                baseProduction = 1600000.0
            )
        )
    }
    
    // Pour le système de sauvegarde/chargement
    fun toMap(): Map<String, Any> {
        return mapOf(
            "cookies" to cookies,
            "cookiesPerClick" to cookiesPerClick,
            "totalCookiesEarned" to totalCookiesEarned,
            "totalClicks" to totalClicks,
            "globalMultiplier" to globalMultiplier,
            "upgrades" to upgrades.map { 
                mapOf(
                    "id" to it.id,
                    "level" to it.level,
                    "unlocked" to it.unlocked
                )
            }
        )
    }
    
    fun fromMap(data: Map<String, Any>) {
        cookies = (data["cookies"] as? Number)?.toLong() ?: 0
        cookiesPerClick = (data["cookiesPerClick"] as? Number)?.toLong() ?: 1
        totalCookiesEarned = (data["totalCookiesEarned"] as? Number)?.toLong() ?: 0
        totalClicks = (data["totalClicks"] as? Number)?.toLong() ?: 0
        globalMultiplier = (data["globalMultiplier"] as? Number)?.toDouble() ?: 1.0
        
        val savedUpgrades = data["upgrades"] as? List<Map<String, Any>> ?: return
        savedUpgrades.forEach { savedUpgrade ->
            val id = (savedUpgrade["id"] as? Number)?.toInt() ?: return@forEach
            val level = (savedUpgrade["level"] as? Number)?.toInt() ?: 0
            val unlocked = savedUpgrade["unlocked"] as? Boolean ?: false
            
            upgrades.find { it.id == id }?.let {
                it.level = level
                it.unlocked = unlocked
            }
        }
        
        recalculateProduction()
    }
} 