package com.hbaez.settings_presentation.settings_overview.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hbaez.core.R
import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.onboarding_presentation.components.SelectableButton

@Composable
fun TimerBottomSheet(
    onNextClick: (time: Int) -> Unit,
    isJump: Boolean
){
    val spacing = LocalSpacing.current

    val time1: String
    val time2: String
    val time3: String
    if(isJump){
        time1 = "5 s"
        time2 = "15 s"
        time3 = "45 s"
    } else {
        time1 = "45 s"
        time2 = "60 s"
        time3 = "90 s"
    }
    val selectedTime = remember { mutableStateOf(time2) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.spaceLarge)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if(isJump) stringResource(id = R.string.timer_jump) else stringResource(id = R.string.default_timer_secs),
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Row {
                SelectableButton(
                    text = time1,
                    isSelected = selectedTime.value == time1,
                    color = MaterialTheme.colorScheme.secondary,
                    selectedTextColor = Color.White,
                    onClick = {
                        selectedTime.value = time1
                    },
                    textStyle = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
                Spacer(modifier = Modifier.width(spacing.spaceMedium))
                SelectableButton(
                    text = time2,
                    isSelected = selectedTime.value == time2,
                    color = MaterialTheme.colorScheme.secondary,
                    selectedTextColor = Color.White,
                    onClick = {
                        selectedTime.value = time2
                    },
                    textStyle = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
                Spacer(modifier = Modifier.width(spacing.spaceMedium))
                SelectableButton(
                    text = time3,
                    isSelected = selectedTime.value == time3,
                    color = MaterialTheme.colorScheme.secondary,
                    selectedTextColor = Color.White,
                    onClick = {
                        selectedTime.value = time3
                    },
                    textStyle = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
        Button(
            onClick = { onNextClick(selectedTime.value.split(" ")[0].toInt()) },
            modifier = Modifier.align(Alignment.BottomEnd),
            shape = RoundedCornerShape(100.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Next"
            )
        }
    }
}