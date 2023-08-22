package com.example.workout_logger_presentation.workout_logger_overview.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core.R
import com.hbaez.user_auth_presentation.model.CompletedWorkout

@Composable
fun CompletedWorkoutItem(
    workout: CompletedWorkout,
    imageUrl: String?,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
    color: Color = MaterialTheme.colorScheme.primary
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    val colorMatrix = floatArrayOf(
        -1f, 0f, 0f, 0f, 255f,
        0f, -1f, 0f, 0f, 255f,
        0f, 0f, -1f, 0f, 255f,
        0f, 0f, 0f, 1f, 0f
    ) // inverts color

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(100f))
            .clickable { onClick() }
//            .border(
//                width = 1.dp,
//                color = color,
//                shape = RoundedCornerShape(100f)
//            )
            .background(color = MaterialTheme.colorScheme.inversePrimary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.spaceMedium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = rememberImagePainter(
                    data = imageUrl,
                    builder = {
                        crossfade(true)
                        error(R.drawable.ic_exercise)
                        fallback(R.drawable.ic_exercise)
                    }
                ),
                contentDescription = workout.exerciseName,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(75.dp)
                    .clip(RoundedCornerShape(topStart = 5.dp))
                    .aspectRatio(1f),
                colorFilter = if(MaterialTheme.colorScheme.onBackground.red * 255 > 150 && MaterialTheme.colorScheme.onBackground.blue * 255 > 150 && MaterialTheme.colorScheme.onBackground.green * 255 > 150) ColorFilter.colorMatrix(ColorMatrix(colorMatrix)) else null
            )
            Spacer(modifier = Modifier.width(spacing.spaceSmall))
            Text(
                text = workout.exerciseName,
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier
                    .weight(1f) // Occupy remaining space
                    .wrapContentWidth()
            )
            Icon(
                imageVector = if (isExpanded) {
                    Icons.Default.KeyboardArrowUp
                } else Icons.Default.KeyboardArrowDown,
                contentDescription = if(isExpanded) {
                    stringResource(id = R.string.collapse)
                } else stringResource(id = R.string.extend)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = spacing.spaceSmall,
                    end = spacing.spaceSmall,
                    bottom = spacing.spaceMedium
                ),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var completed = 0
                workout.isCompleted.forEach { if(it == "true") completed++ }
                Text(
                    text = completed.toString() + "/" + workout.sets.toString() ,
                    fontSize = 20.sp
                )
                Text(
                    text = stringResource(id = R.string.sets),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Light
                )
            }
            Spacer(modifier = Modifier.width(spacing.spaceMedium))
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var totalVolume = 0.0
                var counter = 0
                workout.weight.forEach{
                    if(workout.isCompleted[counter] == "true"){
                        totalVolume += it.trim().toDouble() * workout.reps[counter].trim().toDouble()
                    }
                    counter++
                }
                Text(
                    text = totalVolume.toString(),
                    fontSize = 20.sp
                )
                Text(
                    text = stringResource(id = R.string.total_volume),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Light
                )
            }
            Spacer(modifier = Modifier.width(spacing.spaceMedium))
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var totalReps = 0
                var counter = 0
                workout.reps.forEach{
                    if(workout.isCompleted[counter] == "true"){
                        totalReps += it.trim().toInt()
                    }
                    counter++
                }
                Text(
                    text = totalReps.toString(),
                    fontSize = 20.sp
                )
                Text(
                    text = stringResource(id = R.string.reps),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Light
                )
            }
        }
        AnimatedVisibility(visible = isExpanded) {
            content()
        }
    }
}