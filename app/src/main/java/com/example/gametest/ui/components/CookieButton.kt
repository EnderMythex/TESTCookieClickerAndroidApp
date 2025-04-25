package com.example.gametest.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.gametest.R
import kotlinx.coroutines.launch

/**
 * Composant pour le bouton cookie cliquable
 */
@Composable
fun CookieButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(1f) }
    val coroutineScope = rememberCoroutineScope()
    
    // Fonction pour l'animation de clic
    fun animateCookieClick() {
        coroutineScope.launch {
            // Animation de réduction
            scale.animateTo(
                targetValue = 0.9f,
                animationSpec = tween(
                    durationMillis = 100,
                    easing = FastOutSlowInEasing
                )
            )
            // Animation de retour à la taille normale
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 100,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }
    
    Box(
        modifier = modifier
            .size(200.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false, radius = 100.dp),
                onClick = {
                    onClick()
                    animateCookieClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.cookie),
            contentDescription = "Cookie",
            modifier = Modifier
                .fillMaxSize()
                .scale(scale.value),
            contentScale = ContentScale.Fit
        )
    }
} 