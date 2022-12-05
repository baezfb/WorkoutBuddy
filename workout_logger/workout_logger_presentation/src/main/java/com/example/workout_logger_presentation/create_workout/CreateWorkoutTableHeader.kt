package com.example.workout_logger_presentation.create_workout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core.R

@Composable
fun CreateWorkoutTableHeader(
){
    val spacing = LocalSpacing.current

    Row(
        modifier = Modifier.background(MaterialTheme.colors.background)
    ) {
        TableCell(text = stringResource(id = R.string.exercise), weight = .32f, 12.sp)
        TableCell(text = stringResource(id = R.string.sets), weight = .16f, 10.sp)
        TableCell(text = stringResource(id = R.string.reps), weight = .16f, 10.sp)
        TableCell(text = stringResource(id = R.string.rest), weight = .18f, 10.sp)
        TableCell(text = stringResource(id = R.string.weight), weight = .18f, 10.sp)

    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    fontSize:  TextUnit
){
    Text(
        text = text,
        modifier = Modifier
            .border(1.dp, MaterialTheme.colors.background)
            .weight(weight)
            .padding(8.dp)
            .height(14.dp),
        fontSize = fontSize,
        maxLines = 1,
        textAlign = TextAlign.Center
    )
}
