package com.example.workout_logger_presentation.create_exercise

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter.State.Empty.painter
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import com.example.workout_logger_presentation.components.AddButton
import com.example.workout_logger_presentation.components.NameField
import com.example.workout_logger_presentation.create_workout.CreateWorkoutEvent
import com.hbaez.core.R
import com.hbaez.core.util.UiEvent
import com.hbaez.core_ui.LocalSpacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalCoilApi
@Composable
fun CreateExerciseScreen(
    scaffoldState: ScaffoldState,
    createExercise: Boolean,
    onNavigateUp: () -> Unit,
    viewModel: CreateExerciseViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val colorMatrix = floatArrayOf(
        -1f, 0f, 0f, 0f, 255f,
        0f, -1f, 0f, 0f, 255f,
        0f, 0f, -1f, 0f, 255f,
        0f, 0f, 0f, 1f, 0f
    ) // inverts color

    LaunchedEffect(Unit){
        viewModel.uiEvent.collect{ event ->
            when(event) {
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message.asString(context)
                    )
                }
                is UiEvent.NavigateUp -> onNavigateUp()
                else -> Unit
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(spacing.spaceMedium)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = { /* TODO Handle add picture button click */ },
                shape = CircleShape,
                colors = ButtonDefaults.outlinedButtonColors(backgroundColor = MaterialTheme.colors.background, contentColor = MaterialTheme.colors.primary),
                modifier = Modifier
                    .size(96.dp)
            ) {
                if(state.image_1 == null){
                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.CenterVertically),
                        text = "+\nAdd Picture",
                        style = MaterialTheme.typography.subtitle2,
                        textAlign = TextAlign.Center,
                    )
                }
                else{
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.CenterVertically)
                            .graphicsLayer(shape = CircleShape)
                    ) {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = rememberImagePainter(
                                data = state.image_1,
                                builder = {
                                    crossfade(true)
                                    error(R.drawable.ic_exercise)
                                    fallback(R.drawable.ic_exercise)
                                }
                            ),
                            contentDescription = "main exercise image",
                            colorFilter = ColorFilter.colorMatrix(ColorMatrix(colorMatrix))
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(spacing.spaceSmall))
            NameField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.spaceMedium),
                text = state.exerciseName,
                hint = stringResource(id = R.string.exercise_name),
                onValueChange = {
                    viewModel.onEvent(CreateExerciseEvent.OnUpdateExerciseName(it))
                },
                onFocusChanged = {
//                    viewModel.onEvent(CreateWorkoutEvent.OnWorkoutNameFocusChange(it.isFocused)) /*TODO*/
                },
                keyboardController = keyboardController
            )
        }
        Spacer(modifier = Modifier.height(spacing.spaceMedium))
        NameField(
            modifier = Modifier
                .fillMaxWidth(),
            height = spacing.spaceExtraExtraLarge + spacing.spaceLarge,
            singleLine = false,
            text = state.description,
            hint = stringResource(id = R.string.description),
            onValueChange = {
                    viewModel.onEvent(CreateExerciseEvent.OnUpdateDescription(it))
            },
            onFocusChanged = {
//                    viewModel.onEvent(CreateWorkoutEvent.OnWorkoutNameFocusChange(it.isFocused)) /*TODO*/
            },
            keyboardController = keyboardController
        )
        Spacer(modifier = Modifier.height(spacing.spaceMedium))
        Box(
            modifier = Modifier.wrapContentHeight(),
            contentAlignment = Alignment.Center
        ){
            OutlinedTextField(
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = MaterialTheme.colors.background
                ),
                value = "",
                label = { Text(text = "Add more pictures") } ,
                onValueChange = { /*TODO*/ },
                readOnly = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        defaultKeyboardAction(ImeAction.Done)
                    }
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .background(MaterialTheme.colors.background)
                    .fillMaxWidth()
                    .heightIn(min = spacing.spaceExtraExtraLarge)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                repeat(3) { index ->
                    OutlinedButton(
                        onClick = { /* Handle add picture button click */ },
                        shape = CircleShape,
                        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = MaterialTheme.colors.background, contentColor = MaterialTheme.colors.primary),
                        modifier = Modifier
                            .size(96.dp)
                    ) {
                        val imageState = when (index) {
                            0 -> state.image_2
                            1 -> state.image_3
                            2 -> state.image_4
                            else -> null
                        }
                        if(imageState == null){
                            Icon(
                                modifier = Modifier.fillMaxSize(.6f),
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(id = R.string.add)
                            )
                        }
                        else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.CenterVertically)
                                    .graphicsLayer(shape = CircleShape)
                            ) {
                                Image(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = rememberImagePainter(
                                        data = imageState,
                                        builder = {
                                            crossfade(true)
                                            error(R.drawable.ic_exercise)
                                            fallback(R.drawable.ic_exercise)
                                        }
                                    ),
                                    contentDescription = "main exercise image",
                                    colorFilter = ColorFilter.colorMatrix(ColorMatrix(colorMatrix))
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(spacing.spaceMedium))
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "MUSCLES",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.h6
            )

            OutlinedTextField(
                value = state.filterText,
                onValueChange = {
                    viewModel.onEvent(CreateExerciseEvent.OnUpdateFilter(it))
                                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                placeholder = { Text("Filter") },
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            viewModel.onEvent(CreateExerciseEvent.OnClearFilter)
                        }
                    )
                }

            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "PRIMARY",
//                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = "SECONDARY",
//                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.subtitle1
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(spacing.spaceExtraExtraLarge + spacing.spaceExtraLarge)
                    .background(color = MaterialTheme.colors.surface)
                    .clip(
                        RoundedCornerShape(8.dp)
                    )
                    .border(4.dp, Color.Gray, RoundedCornerShape(4.dp))
            ) {
                items(state.muscles.size) {
                    if(state.muscles[it].name.contains(state.filterText, ignoreCase = true)){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = spacing.spaceSmall),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = state.primaryMuscles.contains(state.muscles[it]),
                                onCheckedChange = { checked ->
                                    if(checked){
                                        viewModel.onEvent(CreateExerciseEvent.OnCheckboxAdd(state.muscles[it], true))
                                    } else {
                                        viewModel.onEvent(CreateExerciseEvent.OnCheckboxRemove(state.muscles[it], true))
                                    }
                                },
                                enabled = !state.secondaryMuscles.contains(state.muscles[it]),
                                modifier = Modifier.padding(end = spacing.spaceSmall)
                            )
                            Text(
                                text = state.muscles[it].name,
                                modifier = Modifier.padding(end = spacing.spaceMedium)
                            )

                            Checkbox(
                                checked = state.secondaryMuscles.contains(state.muscles[it]),
                                onCheckedChange = { checked ->
                                    if(checked){
                                        viewModel.onEvent(CreateExerciseEvent.OnCheckboxAdd(state.muscles[it], false))
                                    } else {
                                        viewModel.onEvent(CreateExerciseEvent.OnCheckboxRemove(state.muscles[it], false))
                                    }
                                },
                                enabled = !state.primaryMuscles.contains(state.muscles[it]),
                                modifier = Modifier.padding(end = spacing.spaceSmall)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(.5f),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_muscular_system_front), //else { painterResource(id = R.drawable.ic_muscular_system_back) },
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(300.dp)
                            .clip(RoundedCornerShape(topStart = 5.dp))
                    )
                    state.primaryMuscles.forEach {
                        if(it.isFront){
                            Image(
                                painter = rememberImagePainter(
                                    data = "https://wger.de${it.imageURL}",
                                    builder = {
                                        crossfade(true)
                                        decoder(SvgDecoder(context = context))
                                    }
                                ),
                                contentDescription = it.name,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(300.dp)
                                    .clip(RoundedCornerShape(topStart = 5.dp))
                            )
                        }
                    }
                    state.secondaryMuscles.forEach {
                        if(it.isFront){
                            Log.println(Log.DEBUG, "test image url","https://wger.de${it.imageURL}")
                            Image(
                                painter = rememberImagePainter(
                                    data = "https://wger.de${it.imageURL}".replace("main", "secondary"),
                                    builder = {
                                        crossfade(true)
                                        decoder(SvgDecoder(context = context))
                                    }
                                ),
                                contentDescription = it.name,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(300.dp)
                                    .clip(RoundedCornerShape(topStart = 5.dp))
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_muscular_system_back), //else { painterResource(id = R.drawable.ic_muscular_system_back) },
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(300.dp)
                            .clip(RoundedCornerShape(topStart = 5.dp))
                    )
                    state.primaryMuscles.forEach {
                        if(!it.isFront){
                            Image(
                                painter = rememberImagePainter(
                                    data = "https://wger.de${it.imageURL}",
                                    builder = {
                                        crossfade(true)
                                        decoder(SvgDecoder(context = context))
                                    }
                                ),
                                contentDescription = it.name,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(300.dp)
                                    .clip(RoundedCornerShape(topStart = 5.dp))
                            )
                        }
                    }
                    state.secondaryMuscles.forEach {
                        if(!it.isFront){
                            Image(
                                painter = rememberImagePainter(
                                    data = "https://wger.de${it.imageURL}".replace("main", "secondary"),
                                    builder = {
                                        crossfade(true)
                                        decoder(SvgDecoder(context = context))
                                    }
                                ),
                                contentDescription = it.name,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(300.dp)
                                    .clip(RoundedCornerShape(topStart = 5.dp))
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            AddButton(
                text = stringResource(id = if(createExercise) R.string.submit else R.string.update),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = spacing.spaceExtraExtraLarge, end = spacing.spaceSmall),
                onClick = {
                    if(createExercise) viewModel.onEvent(CreateExerciseEvent.OnSubmitExercise)
                    else viewModel.onEvent(CreateExerciseEvent.OnUpdateExercise)
                },
                icon = Icons.Default.Done
            )
        }
    }
}
