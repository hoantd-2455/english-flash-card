package com.englishflashcard.ui.screens.lessonlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.englishflashcard.domain.model.Lesson
import com.englishflashcard.ui.theme.DeepError
import com.englishflashcard.ui.theme.GradientDeepGreen
import com.englishflashcard.ui.theme.GradientLightGreen
import com.englishflashcard.ui.theme.GradientMidGreen
import com.englishflashcard.ui.theme.GlassLight
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val MAX_DESCRIPTION_LINES = 2

/**
 * Card với liquid glass effect - iOS style
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonCard(
    lesson: Lesson,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Deep green gradient
    val deepGreenGradient = Brush.linearGradient(
        colors = listOf(
            GradientDeepGreen,
            GradientMidGreen,
            GradientLightGreen
        )
    )

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(32.dp), // Liquid glass - very rounded
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        // Glass morphism effect với gradient background
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(deepGreenGradient)
                .background(GlassLight) // Glass overlay
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = lesson.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                lesson.description?.let { desc ->
                    if (desc.isNotBlank()) {
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = MAX_DESCRIPTION_LINES,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.White.copy(alpha = 0.95f)
                        )
                    }
                }

                Text(
                    text = formatCreatedDate(lesson.createdAt),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }

            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete lesson",
                    tint = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }

    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            lessonTitle = lesson.title,
            onConfirm = {
                onDelete()
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}

private fun formatCreatedDate(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return "Created: ${dateFormat.format(Date(timestamp))}"
}

@Composable
private fun DeleteConfirmationDialog(
    lessonTitle: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Lesson?") },
        text = {
            Text("Are you sure you want to delete \"$lessonTitle\"? This action cannot be undone.")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Delete", color = DeepError)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
