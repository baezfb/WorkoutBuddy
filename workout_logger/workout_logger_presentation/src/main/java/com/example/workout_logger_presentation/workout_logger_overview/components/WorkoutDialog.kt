package com.example.workout_logger_presentation.workout_logger_overview.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
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
            .clip(RoundedCornerShape(50.dp)),
        onDismissRequest = { onDismiss() },
        content = {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxHeight(),
                content = {
                    Column {
                        Spacer(modifier = Modifier.height(spacing.spaceLarge))
                        Text(
                            stringResource(id = R.string.choose_workout),
                            textAlign = TextAlign.Center,
                            fontSize = 32.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        )
                        Spacer(modifier = Modifier.height(spacing.spaceMedium))
                        LazyColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            contentPadding = PaddingValues(vertical = spacing.spaceMedium),
                            modifier = Modifier.heightIn(250.dp)
                        ){
                            val routineNames = mutableListOf<String>()
                            val workoutId = hashMapOf<String, List<Int>>()
                            workoutTemplates.value.forEach {
                                if(routineNames.contains(it.name)) {
                                    val tmp = workoutId[it.name]!!.toMutableList()
                                    if(!tmp.contains(it.rowId)){
                                        tmp.add(it.rowId)
                                        workoutId[it.name] = tmp.toList()
                                    }
                                }
                                else{
                                    routineNames.add(it.name)
                                    val tmp = listOf(it.rowId)
                                    workoutId[it.name] = tmp
                                }
                            }
                            Log.println(Log.DEBUG, "workout names", routineNames.toString())
                            Log.println(Log.DEBUG, "workout ids", workoutId.toString())
                            if(routineNames.size == 0){
                                item {
                                    Text(
                                        stringResource(id = R.string.no_routines),
                                        textAlign = TextAlign.Center,
                                        fontSize = 32.sp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight()
                                    )
                                }
                            }
                            items(routineNames.size){
                                AddButton(
                                    text = routineNames[it],
                                    onClick = { onChooseWorkout(routineNames[it], workoutId[routineNames[it]]!!.toString()) },
                                    icon = Icons.Default.List,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(spacing.spaceSmall)
                                )
                            }
                        }
                    }
                }
            )
               }
    )
}