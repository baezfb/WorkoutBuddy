package com.hbaez.onboarding_presentation.age

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.hbaez.core.R
import com.hbaez.core.util.UiEvent
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.onboarding_presentation.components.ActionButton
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgeScreen(
    snackBarHost: SnackbarHostState,
    onNextClick: () -> Unit,
    viewModel: AgeViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    val showDatePicker = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> onNextClick()
                is UiEvent.ShowSnackbar -> {
                    snackBarHost.showSnackbar(
                        message = event.message.asString(context)
                    )
                }
                else -> Unit
            }
        }
    }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = if(viewModel.age != -1L) viewModel.age else Instant.now().toEpochMilli(),
        initialDisplayMode = DisplayMode.Input
    )
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
                text = stringResource(id = R.string.dob),
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Text(
                text = Instant.ofEpochMilli(viewModel.age).atZone(ZoneId.systemDefault()).toLocalDate().toString(),
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(spacing.spaceMedium)
                    .clickable {
                        showDatePicker.value = !showDatePicker.value
                    }
            )
        }
        ActionButton(
            text = stringResource(id = R.string.next),
            onClick = viewModel::onNextClick,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
        if(showDatePicker.value){
            DatePickerDialog(
                shape = RoundedCornerShape(spacing.spaceSmall),
                onDismissRequest = { showDatePicker.value = !showDatePicker.value },
                confirmButton = {
                    ActionButton(
                        text = stringResource(id = R.string.next),
                        onClick = {
                            if(datePickerState.selectedDateMillis != null){
                                viewModel.onAgeEnter(datePickerState.selectedDateMillis!! + ZoneId.systemDefault().rules.getOffset(Instant.now()).totalSeconds*1000)
                            } else
                                viewModel.onAgeEnter(LocalDate.parse(
                                LocalDate.now().toString()).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                            )
                            showDatePicker.value = !showDatePicker.value
                        },
                        modifier = Modifier.align(Alignment.BottomEnd)
                    )
                },
            ) {
                DatePicker(
                    state = datePickerState,
                    dateValidator = { timestamp ->
                        timestamp <= Instant.now().toEpochMilli()
                    }
                )
            }
        }
    }
}