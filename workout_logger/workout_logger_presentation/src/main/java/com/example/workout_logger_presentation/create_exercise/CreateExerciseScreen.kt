package com.example.workout_logger_presentation.create_exercise

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter.State.Empty.painter
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import com.example.workout_logger_presentation.components.NameField
import com.hbaez.core.R
import com.hbaez.core_ui.LocalSpacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalCoilApi
@Composable
fun CreateExerciseScreen() {
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

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
                onClick = { /* Handle add picture button click */ },
                shape = CircleShape,
                colors = ButtonDefaults.outlinedButtonColors(backgroundColor = MaterialTheme.colors.background, contentColor = MaterialTheme.colors.primary),
                modifier = Modifier
                    .size(96.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterVertically),
                    text = "+\nAdd Picture",
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(modifier = Modifier.width(spacing.spaceSmall))
            NameField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.spaceMedium),
                text = "", /*TODO*/
                hint = stringResource(id = R.string.exercise_name),
                onValueChange = {
//                    viewModel.onEvent(CreateWorkoutEvent.OnWorkoutNameChange(it)) /*TODO*/
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
                .fillMaxWidth()
                .padding(spacing.spaceMedium),
            height = spacing.spaceExtraExtraLarge + spacing.spaceLarge,
            singleLine = false,
            text = "", /*TODO*/
            hint = stringResource(id = R.string.description),
            onValueChange = {
//                    viewModel.onEvent(CreateWorkoutEvent.OnWorkoutNameChange(it)) /*TODO*/
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
                repeat(3) {
                    OutlinedButton(
                        onClick = { /* Handle add picture button click */ },
                        shape = CircleShape,
                        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = MaterialTheme.colors.background, contentColor = MaterialTheme.colors.primary),
                        modifier = Modifier
                            .size(96.dp)
                    ) {
                        Icon(
                            modifier = Modifier.fillMaxSize(.6f),
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(id = R.string.add)
                        )
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
                value = "",
                onValueChange = { /* TODO: Handle filter search bar value change */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                placeholder = { Text("Filter") },
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
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
                    .size(spacing.spaceExtraExtraLarge + spacing.spaceExtraExtraLarge)
                    .background(color = MaterialTheme.colors.surface)
                    .clip(
                        RoundedCornerShape(8.dp)
                    )
                    .border(3.dp, MaterialTheme.colors.primary, RoundedCornerShape(6.dp))
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = true, /*TODO*/
                            onCheckedChange = { /*TODO*/ },
                            modifier = Modifier.padding(end = spacing.spaceSmall)
                        )
                        Text(
                            text = "Biceps Brachii", /*TODO*/
                            modifier = Modifier.padding(end = spacing.spaceMedium)
                        )

                        Checkbox(
                            checked = false, /*TODO*/
                            onCheckedChange = { /*TODO*/ },
                            modifier = Modifier.padding(end = spacing.spaceSmall)
                        )
                    }
                    repeat(20) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = false, /*TODO*/
                                onCheckedChange = { /*TODO*/ },
                                modifier = Modifier.padding(end = spacing.spaceSmall)
                            )
                            Text(
                                text = "Biceps Brachii", /*TODO*/
                                modifier = Modifier.padding(end = spacing.spaceMedium)
                            )

                            Checkbox(
                                checked = false, /*TODO*/
                                onCheckedChange = { /*TODO*/ },
                                modifier = Modifier.padding(end = spacing.spaceSmall)
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = if(true /*TODO*/){ painterResource(id = R.drawable.ic_muscular_system_front) } else { painterResource(id = R.drawable.ic_muscular_system_back) },
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(300.dp)
                        .clip(RoundedCornerShape(topStart = 5.dp))
                )
                if(true /*TODO*/){
                    Image(
                            painter = rememberImagePainter(
                                data = "https://wger.de/static/images/muscles/main/muscle-1.svg",
                                builder = {
                                    crossfade(true)
                                    decoder(SvgDecoder(context = context))
                                }
                            ),
                            contentDescription = "Biceps Brachii",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(300.dp)
                                .clip(RoundedCornerShape(topStart = 5.dp))
                        )
//                    exercise.image_url_main.onEach {
//                        Log.println(Log.DEBUG, "!!!!!!!!!!!!", ("https://wger.de$it").toString())
//                        Image(
//                            painter = rememberImagePainter(
//                                data = "https://wger.de$it",
//                                builder = {
//                                    crossfade(true)
//                                    decoder(SvgDecoder(context = context))
//                                }
//                            ),
//                            contentDescription = it ?: exercise.name,
//                            contentScale = ContentScale.Fit,
//                            modifier = Modifier
//                                .size(200.dp)
//                                .clip(RoundedCornerShape(topStart = 5.dp))
//                        )
//                    }
                }
                if(true/*TODO*/){
                    Image(
                        painter = rememberImagePainter(
                            data = "https://wger.de/static/images/muscles/secondary/muscle-13.svg",
                            builder = {
                                crossfade(true)
                                decoder(SvgDecoder(context = context))
                            }
                        ),
                        contentDescription = "Brachialis",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(300.dp)
                            .clip(RoundedCornerShape(topStart = 5.dp))
                    )
//                    exercise.image_url_secondary.onEach {
//                        Log.println(Log.DEBUG, "!!!!!!!!!!!!", ("https://wger.de$it").toString())
//                    }
                }
            }
        }
    }
}
