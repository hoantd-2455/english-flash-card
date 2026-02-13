package com.englishflashcard.ui.screens.lessonlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.englishflashcard.ui.theme.DeepGreen
import com.englishflashcard.ui.theme.LightGreen

private const val ICON_SIZE_DP = 72

/**
 * Empty state với deep green theme
 */
@Composable
fun EmptyState(
    onCreateLesson: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier.height(ICON_SIZE_DP.dp),
            tint = DeepGreen
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No Lessons Yet",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            color = DeepGreen
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Create your first vocabulary lesson to start learning",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onCreateLesson,
            shape = RoundedCornerShape(24.dp), // Liquid glass style
            colors = ButtonDefaults.buttonColors(
                containerColor = LightGreen,
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 6.dp,
                pressedElevation = 12.dp
            )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text("Add Lesson")
        }
    }
}
