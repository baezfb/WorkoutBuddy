package com.example.workout_logger_presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hbaez.core_ui.LocalSpacing

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NameField(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = LocalSpacing.current.spaceMedium,
    singleLine: Boolean = true,
    hint: String,
    shouldShowHint: Boolean = false,
    onFocusChanged: (FocusState) -> Unit,
    keyboardController: SoftwareKeyboardController?
){
    val spacing = LocalSpacing.current
    Box(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = text,
            label = { Text(text = hint) } ,
            onValueChange = onValueChange,
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    defaultKeyboardAction(ImeAction.Done)
                }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.inverseOnSurface
            ),
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(5.dp)
                )
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .heightIn(min = height)
                .onFocusChanged { onFocusChanged(it) }
                .testTag("workoutname_textfield")
        )
        if(shouldShowHint) {
            Text(
                text = hint,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Light,
                color = Color.White,
                modifier = Modifier
//                    .align(Alignment.CenterStart)
                    .padding(start = spacing.spaceMedium)
            )
        }
    }
}