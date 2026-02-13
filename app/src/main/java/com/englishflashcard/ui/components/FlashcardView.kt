package com.englishflashcard.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.englishflashcard.ui.theme.GradientDeepGreen
import com.englishflashcard.ui.theme.GradientLightGreen
import com.englishflashcard.ui.theme.GradientMidGreen
import com.englishflashcard.ui.theme.GlassLight
import com.englishflashcard.ui.theme.LightGreen
import com.englishflashcard.ui.theme.MediumGreen

private const val FLIP_ANIMATION_DURATION = 400
private const val CARD_HEIGHT_DP = 300

/**
 * Flashcard với liquid glass effect - iOS style
 */
@Composable
fun FlashcardView(
    frontText: String,
    backText: String,
    isFlipped: Boolean,
    onFlip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(
            durationMillis = FLIP_ANIMATION_DURATION,
            easing = FastOutSlowInEasing
        ),
        label = "card_flip"
    )

    // Front gradient - Deep green
    val frontGradient = Brush.verticalGradient(
        colors = listOf(
            GradientDeepGreen,
            GradientMidGreen,
            GradientLightGreen
        )
    )

    // Back gradient - Medium to light green
    val backGradient = Brush.verticalGradient(
        colors = listOf(
            MediumGreen,
            LightGreen,
            GradientLightGreen
        )
    )

    Card(
        modifier = modifier
            .height(CARD_HEIGHT_DP.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clickable(onClick = onFlip),
        shape = RoundedCornerShape(40.dp), // Extra rounded - liquid glass
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (rotation <= 90f) frontGradient else backGradient)
                .background(GlassLight) // Glass overlay
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            // Show front or back based on rotation
            if (rotation <= 90f) {
                // Front side (English)
                Text(
                    text = frontText,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier.graphicsLayer {
                        rotationY = 0f
                    }
                )
            } else {
                // Back side (Vietnamese)
                Text(
                    text = backText,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier.graphicsLayer {
                        rotationY = 180f
                    }
                )
            }
        }
    }
}
