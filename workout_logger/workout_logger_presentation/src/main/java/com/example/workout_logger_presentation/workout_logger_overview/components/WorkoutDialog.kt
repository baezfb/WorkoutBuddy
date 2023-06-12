package com.example.workout_logger_presentation.workout_logger_overview.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.example.workout_logger_presentation.components.AddButton
import com.hbaez.core.R
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.user_auth_presentation.model.WorkoutTemplate

@ExperimentalCoilApi
@Composable
fun WorkoutDialog(
    onDismiss: () -> Unit,
    onChooseWorkout: (workoutName: String, workoutIds: String) -> Unit,
    modifier: Modifier = Modifier,
    workoutNames: List<String>,
    workoutTemplates: State<List<WorkoutTemplate>>
) {
    val spacing = LocalSpacing.current
    AlertDialog(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 275.dp)
            .wrapContentHeight(align = Alignment.CenterVertically)
            .clip( RoundedCornerShape(50.dp) ),
        onDismissRequest = { onDismiss() },
        title = null,
        text = null,
        buttons = {
            Scaffold(
                backgroundColor = MaterialTheme.colors.surface,
                modifier = Modifier.fillMaxHeight(),
                topBar = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            stringResource(id = R.string.choose_workout),
                            textAlign = TextAlign.Center,
                            fontSize = 32.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        )
                        Spacer(modifier = Modifier.height(spacing.spaceLarge))
                    }
                },
                content = {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        contentPadding = PaddingValues(vertical = spacing.spaceMedium),
                        modifier = Modifier.heightIn(250.dp)
                    ){
                        val uniqueNames = mutableListOf<String>()
                        val workoutId = hashMapOf<String, List<Int>>()
                        workoutTemplates.value.forEach {
                            if(uniqueNames.contains(it.name)) {
                                val tmp = workoutId[it.name]!!.toMutableList()
                                tmp.add(it.rowId)
                                workoutId[it.name] = tmp.toList()
                                return@forEach
                            }
                            uniqueNames.add(it.name)
                            val tmp = listOf(it.rowId)
                            workoutId[it.name] = tmp
                        }
                        Log.println(Log.DEBUG, "workout names", uniqueNames.toString())
                        Log.println(Log.DEBUG, "workout ids", workoutId.toString())
                        items(uniqueNames.size){
                            AddButton(
                                text = uniqueNames[it],
                                onClick = { onChooseWorkout(uniqueNames[it], workoutId[uniqueNames[it]]!!.toString()) },
                                icon = Icons.Default.List,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(spacing.spaceSmall)
                            )
                        }
                    }
                }
            )
               }
    )
}