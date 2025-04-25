package com.example.gametest.data

import android.content.Context
import android.content.SharedPreferences
import com.example.gametest.model.GameState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

/**
 * Gestionnaire de données du jeu qui s'occupe de sauvegarder et charger l'état du jeu
 */
class GameDataManager(private val context: Context) {
    
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    /**
     * Sauvegarde l'état du jeu
     */
    suspend fun saveGameState(gameState: GameState) = withContext(Dispatchers.IO) {
        val gameData = gameState.toMap()
        val jsonObject = JSONObject()
        
        // Sauvegarder les valeurs simples
        jsonObject.put("cookies", gameData["cookies"] as Long)
        jsonObject.put("cookiesPerClick", gameData["cookiesPerClick"] as Long)
        jsonObject.put("totalCookiesEarned", gameData["totalCookiesEarned"] as Long)
        jsonObject.put("totalClicks", gameData["totalClicks"] as Long)
        jsonObject.put("globalMultiplier", gameData["globalMultiplier"] as Double)
        
        // Sauvegarder les améliorations
        val upgradesArray = JSONArray()
        val upgrades = gameData["upgrades"] as List<Map<String, Any>>
        for (upgrade in upgrades) {
            val upgradeObject = JSONObject()
            upgradeObject.put("id", upgrade["id"] as Int)
            upgradeObject.put("level", upgrade["level"] as Int)
            upgradeObject.put("unlocked", upgrade["unlocked"] as Boolean)
            upgradesArray.put(upgradeObject)
        }
        jsonObject.put("upgrades", upgradesArray)
        
        // Sauvegarder le JSON
        sharedPreferences.edit().putString(KEY_GAME_STATE, jsonObject.toString()).apply()
    }
    
    /**
     * Charge l'état du jeu
     */
    suspend fun loadGameState(gameState: GameState): Boolean = withContext(Dispatchers.IO) {
        val jsonString = sharedPreferences.getString(KEY_GAME_STATE, null) ?: return@withContext false
        
        try {
            val jsonObject = JSONObject(jsonString)
            val gameData = mutableMapOf<String, Any>()
            
            // Charger les valeurs simples
            gameData["cookies"] = jsonObject.getLong("cookies")
            gameData["cookiesPerClick"] = jsonObject.getLong("cookiesPerClick")
            gameData["totalCookiesEarned"] = jsonObject.getLong("totalCookiesEarned")
            gameData["totalClicks"] = jsonObject.getLong("totalClicks")
            gameData["globalMultiplier"] = jsonObject.getDouble("globalMultiplier")
            
            // Charger les améliorations
            val upgradesArray = jsonObject.getJSONArray("upgrades")
            val upgrades = mutableListOf<Map<String, Any>>()
            
            for (i in 0 until upgradesArray.length()) {
                val upgradeObject = upgradesArray.getJSONObject(i)
                val upgrade = mutableMapOf<String, Any>()
                upgrade["id"] = upgradeObject.getInt("id")
                upgrade["level"] = upgradeObject.getInt("level")
                upgrade["unlocked"] = upgradeObject.getBoolean("unlocked")
                upgrades.add(upgrade)
            }
            
            gameData["upgrades"] = upgrades
            
            // Mettre à jour l'état du jeu
            gameState.fromMap(gameData)
            return@withContext true
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext false
        }
    }
    
    /**
     * Réinitialise toutes les données du jeu
     */
    suspend fun resetGameData() = withContext(Dispatchers.IO) {
        sharedPreferences.edit().remove(KEY_GAME_STATE).apply()
    }
    
    companion object {
        private const val PREFS_NAME = "cookie_clicker_prefs"
        private const val KEY_GAME_STATE = "game_state"
    }
} 