package com.example.workout_logger_presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core.R

@Composable
fun SearchTextField(
    text: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    hint: String = stringResource(id = R.string.search),
    shouldShowHint: Boolean = false,
    onFocusChanged: (FocusState) -> Unit
) {
    val spacing = LocalSpacing.current
    Box(
        modifier = modifier
    ) {
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardActions = KeyboardActions(
                onSearch = {
                    onClear()
                    defaultKeyboardAction(ImeAction.Search)
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
            textStyle = TextStyle(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .padding(2.dp)
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(5.dp)
                )
                .background(backgroundColor)
                .border(border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface), shape = RectangleShape)
                .fillMaxWidth()
                .padding(spacing.spaceMedium)
                .padding(end = spacing.spaceMedium)
                .onFocusChanged { onFocusChanged(it) }
                .testTag("search_textfield")
        )
        if(shouldShowHint) {
            Text(
                text = hint,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Light,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = spacing.spaceMedium)
            )
        }
        IconButton(
            icon = Icons.Default.Clear,
            color = MaterialTheme.colorScheme.onBackground,
            onClick = onClear,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}