package com.hbaez.workoutbuddy.workout.start_workout

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Icon
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core.R
import com.hbaez.workoutbuddy.components.WearButton
import com.hbaez.workoutbuddy.components.WearText

@Composable
fun SetCard(
    exerciseName: String,
    page: Int,
    currReps: String,
    currWeight: String,
    onRepIncrease: () -> Unit,
    onRepDecrease: () -> Unit,
    onWeightIncrease: () -> Unit,
    onWeightDecrease: () -> Unit,
    onRest: () -> Unit
){
    val spacing = LocalSpacing.current
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier
            .clip(
                RoundedCornerShape(50.dp)
            )
            .border(2.dp, MaterialTheme.colors.primary, RoundedCornerShape(50.dp))
            .padding(spacing.spaceSmall)
            .fillMaxWidth(.9f)
            .height(155.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WearText(
                modifier = Modifier.padding(horizontal = spacing.spaceSmall),
                color = MaterialTheme.colors.onBackground,
                text = exerciseName,
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(spacing.spaceSmall))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ){
                WearButton(
                    text = "",
                    onClick = { onRepDecrease() },
                    icon = Icons.Rounded.ArrowDownward,
                    borderColor = MaterialTheme.colors.background,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .alignByBaseline()
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .alignByBaseline()
                ) {
                    WearText(
                        text = currReps,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onBackground
                    )
                    WearText(
                        text = stringResource(id = R.string.reps),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onBackground
                    )

                }
                WearButton(
                    text = "",
                    onClick = { onRepIncrease() },
                    icon = Icons.Rounded.ArrowUpward,
                    borderColor = MaterialTheme.colors.background,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .alignByBaseline()
                )
            }
            Spacer(modifier = Modifier.height(spacing.spaceSmall))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ){
                WearButton(
                    text = "",
                    onClick = { onWeightDecrease() },
                    icon = Icons.Rounded.ArrowDownward,
                    borderColor = MaterialTheme.colors.background,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .alignByBaseline()
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .alignByBaseline()
                ) {
                    WearText(
                        text = currWeight,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onBackground
                    )
                    WearText(
                        text = stringResource(id = R.string.lbs),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onBackground
                    )

                }
                WearButton(
                    text = "",
                    onClick = { onWeightIncrease() },
                    icon = Icons.Rounded.ArrowUpward,
                    borderColor = MaterialTheme.colors.background,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .alignByBaseline()
                )
            }
            Spacer(modifier = Modifier.height(spacing.spaceSmall))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp)
            ){
                WearButton(
                    text = stringResource(id = R.string.rest),
                    onClick = { onRest() },
                    icon = Icons.Rounded.Done,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    padding = 0.dp
                )
            }
        }
    }
}