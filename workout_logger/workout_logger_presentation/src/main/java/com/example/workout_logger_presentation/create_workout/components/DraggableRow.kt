package com.example.workout_logger_presentation.create_workout.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.workout_logger_presentation.create_workout.CreateWorkoutTableRow
import com.hbaez.core_ui.LocalSpacing
import kotlin.math.roundToInt

const val ANIMATION_DURATION = 1000
const val MIN_DRAG_AMOUNT = 6

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun DraggableRow(
    name: String,
    sets: String,
    reps: String,
    rest: String,
    weight: String,
    isRevealed: Boolean,
    isSearchRevealed: Boolean,
    hasExercise: Boolean,
    id: Int,
    cardOffset: Float,
    onExpand: (Int) -> Unit,
    onCollapse: (Int) -> Unit,
    onCenter: (Int) -> Unit,
    onNameChange: (String) -> Unit,
    onSetsChange: (String) -> Unit,
    onRepsChange: (String) -> Unit,
    onRestChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onSearchClick: () -> Unit
){
    val spacing = LocalSpacing.current
    val offsetX = remember { mutableStateOf(0f) }
    val transitionState = remember {
        MutableTransitionState(isRevealed).apply {
            targetState = !isRevealed
        }
    }
    val transition = updateTransition(targetState = transitionState, "rowTransition")
    val offsetTransition by transition.animateFloat(
        label = "rowOffsetTransition",
        transitionSpec = { tween(durationMillis = ANIMATION_DURATION) },
        targetValueByState = {
            if (isRevealed and !isSearchRevealed) (cardOffset - offsetX.value)
            else if (isRevealed and isSearchRevealed) (-cardOffset - offsetX.value)
            else -offsetX.value
                             },
    )


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .border(BorderStroke(1.dp, MaterialTheme.colors.onBackground))
            .padding(8.dp)
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
                .padding(start=5.dp, end=5.dp)
        ) {
            Box(modifier = Modifier
                .width(150.dp)
                .fillMaxHeight()
                .clip(RectangleShape)
                .background(MaterialTheme.colors.primaryVariant)
                .clickable {
                    onDeleteClick()
                }
            ){
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(IntrinsicSize.Max))
            Box(modifier = Modifier
                .width(150.dp)
                .fillMaxHeight()
                .clip(RectangleShape)
                .background(Color(0xFF4C62F5))
                .clickable {
                    onSearchClick()
                }
            ){
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.Center)
                )
            }
        }

        Row(
            modifier = Modifier
                .offset { IntOffset((offsetX.value.roundToInt() + offsetTransition).toInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        val original = Offset(offsetX.value, 0f)
                        val summed = original + Offset(x = dragAmount, y = 0f)
                        val newValue = Offset(x = summed.x.coerceIn(-cardOffset, cardOffset), y = 0f)
                        change.consumePositionChange()
                        offsetX.value = newValue.x
                        Log.println(Log.DEBUG, "dragamnt,offsetX.value", dragAmount.toString() + " , " + offsetX.value.toString())
                        when {
                            dragAmount > 10f && offsetX.value in cardOffset*0.5f..cardOffset -> onExpand(id)
                            dragAmount > 10f && offsetX.value in -cardOffset..0f -> onCenter(id)
                            dragAmount < -10f && offsetX.value in -cardOffset..-cardOffset*0.5f -> onCollapse(id)
                            dragAmount < -10f && offsetX.value in 0f..cardOffset -> onCenter(id)
                        }
                    }
                }
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
        ){
            CreateWorkoutTableRow(
                onNameChange,
                onSetsChange,
                onRepsChange,
                onRestChange,
                onWeightChange,
                name = name,
                sets = sets,
                reps = reps,
                rest = rest,
                weight = weight,
                isRevealed = isRevealed || isSearchRevealed,
                hasExercise = hasExercise
            )
        }
    }

}
