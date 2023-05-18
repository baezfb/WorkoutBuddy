package com.hbaez.workoutbuddy.workout.start_workout

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.rounded.ArrowUpward
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
import com.hbaez.workoutbuddy.R
import com.hbaez.workoutbuddy.components.WearText

@Composable
fun SetCard(
    exerciseName: String,

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
            .padding(spacing.spaceMedium)
            .fillMaxWidth(.8f)
            .height(150.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WearText(color = MaterialTheme.colors.onBackground, text = exerciseName, maxLines = 1)
            Spacer(modifier = Modifier.height(spacing.spaceSmall))

            Row(
              Modifier.fillMaxWidth()
            ){
                IncDecChip()
            }
        }
    }
}

@Composable
fun IncDecChip(
    modifier: Modifier = Modifier
) {
    Chip(
        modifier = Modifier.wrapContentSize(),
        onClick = { /*TODO*/ },
        icon = {
            Icon(
                imageVector = Icons.Rounded.ArrowUpward,
                contentDescription = "increase count",
                modifier = modifier.size(24.dp)
            )
        }
    )
}