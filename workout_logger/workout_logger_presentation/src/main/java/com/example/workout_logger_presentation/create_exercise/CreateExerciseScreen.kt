package com.example.workout_logger_presentation.create_exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.example.workout_logger_presentation.components.NameField
import com.hbaez.core.R
import com.hbaez.core_ui.LocalSpacing

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
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "PRIMARY",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = "SECONDARY",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
    }
}
