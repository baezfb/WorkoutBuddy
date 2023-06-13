package com.example.workout_logger_presentation.workout_logger_overview.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core.R
import com.hbaez.user_auth_presentation.model.CompletedWorkout

@Composable
fun CompletedWorkoutItem(
    workout: CompletedWorkout,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
    color: Color = MaterialTheme.colors.primary
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(100f))
            .clickable { onClick() }
//            .border(
//                width = 1.dp,
//                color = color,
//                shape = RoundedCornerShape(100f)
//            )
            .background(color = MaterialTheme.colors.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.spaceMedium),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = workout.exerciseName,
                style = MaterialTheme.typography.h3
            )
            Icon(
                imageVector = if (isExpanded) {
                    Icons.Default.KeyboardArrowUp
                } else Icons.Default.KeyboardArrowDown,
                contentDescription = if(isExpanded) {
                    stringResource(id = com.hbaez.core.R.string.collapse)
                } else stringResource(id = com.hbaez.core.R.string.extend)
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
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Light
                )
            }
            Spacer(modifier = Modifier.width(spacing.spaceMedium))
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var totalVolume = 0
                var counter = 0
                workout.weight.forEach{
                    if(workout.isCompleted[counter] == "true"){
                        totalVolume += it.trim().toInt()
                    }
                    counter++
                }
                Text(
                    text = totalVolume.toString(),
                    fontSize = 20.sp
                )
                Text(
                    text = stringResource(id = R.string.total_volume),
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.body1,
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
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Light
                )
            }
        }
        AnimatedVisibility(visible = isExpanded) {
            content()
        }
    }
}