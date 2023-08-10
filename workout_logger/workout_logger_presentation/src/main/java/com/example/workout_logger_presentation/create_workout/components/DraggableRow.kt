package com.example.workout_logger_presentation.create_workout.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.workout_logger_presentation.create_workout.CreateWorkoutTableRow
import com.hbaez.core_ui.LocalSpacing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

const val ANIMATION_DURATION = 1000
const val MIN_DRAG_AMOUNT = 6

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun DraggableRow(
    sets: String,
    reps: String,
    rest: String,
    weight: String,
    hasExercise: Boolean,
    id: Int,
    cardOffset: Float,
    onRestChange: (text: String) -> Unit,
    onRepsChange: (text: String) -> Unit,
    onWeightChange: (text: String) -> Unit,
    onDeleteRow: (Int) -> Unit
){
    val spacing = LocalSpacing.current
    var isDragging by remember { mutableStateOf(false) }
    var isDismissed by remember { mutableStateOf(false) }

    var center by remember {
        mutableStateOf(Offset.Zero)
    }
    var rowTopLeft by remember {
        mutableStateOf(Offset.Zero)
    }
    var dragStartedPosition by remember {
        mutableStateOf(0f)
    }
    var oldPosition by remember {
        mutableStateOf(rowTopLeft.x)
    }
    val transitionState = remember {
        MutableTransitionState(isDismissed).apply {
            targetState = !isDismissed
        }
    }
    val transition = updateTransition(targetState = transitionState, "rowTransition")
    val offsetTransition by transition.animateFloat(
        label = "rowOffsetTransition",
        transitionSpec = { tween(durationMillis = ANIMATION_DURATION) },
        targetValueByState = {
            if(!isDragging){
                if(isDismissed) (-cardOffset)
                else -rowTopLeft.x
            } else 0f
                             },
    )

    LaunchedEffect(isDismissed) {
        if (isDismissed) {
            delay(500L) // Wait for 1 second
            withContext(Dispatchers.Main) {
                isDismissed = false // Reset isDismissed to false
                onDeleteRow(id)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.background), RoundedCornerShape(4.dp))
            .padding(horizontal = 2.dp)
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.spaceSmall)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RectangleShape)
                .background(MaterialTheme.colorScheme.secondary)
            ){
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterEnd)
                        .padding(horizontal = spacing.spaceMedium)
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(spacing.spaceSmall)
                .offset {
                    IntOffset(
                        (rowTopLeft.x.roundToInt() + offsetTransition.roundToInt()),
                        0
                    )
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            dragStartedPosition = center.x + offset.x
                            isDragging = true

                        },
                        onDragEnd = {
                            oldPosition = rowTopLeft.x
                            isDragging = false
                        }
                    ) { change, dragAmount ->
                        val touchPosition = center.x + change.position.x
                        val newPosition = if (dragAmount.x < 0) {
                            oldPosition + (touchPosition - dragStartedPosition)
                        } else {
                            oldPosition
                        }

                        rowTopLeft = Offset(
                            newPosition.coerceIn(
                                minimumValue = center.x - cardOffset * 1.25f,
                                maximumValue = center.x
                            ),
                            rowTopLeft.y
                        )

                        if (rowTopLeft.x < -cardOffset * .55f && !isDismissed) {
                            isDismissed = true
                        }
                    }
                }
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
        ){
            CreateWorkoutTableRow(
                onRepsChange = { onRepsChange(it) },
                onRestChange = { onRestChange(it) },
                onWeightChange = { onWeightChange(it) },
                sets = sets,
                reps = reps,
                rest = rest,
                weight = weight,
                hasExercise = hasExercise
            )
        }
    }

}
