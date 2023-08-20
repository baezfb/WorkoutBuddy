package com.hbaez.onboarding_presentation.height

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.core.domain.use_case.FilterOutDigits
import com.hbaez.core.util.UiEvent
import com.hbaez.core.util.UiText
import com.hbaez.core.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeightViewModel @Inject constructor(
    private val preferences: Preferences,
    private val filterOutDigits: FilterOutDigits
): ViewModel() {

    var height by mutableStateOf("64")
        private set
    var initHeight = -1

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onHeightChange(height: String) {
        this.height = filterOutDigits(height)
    }

    init {
        if (preferences.loadUserInfo().height != -1){
            initHeight = preferences.loadUserInfo().height
            height = preferences.loadUserInfo().height.toString()
        } else {
            initHeight = height.toInt()
        }
    }

    fun onNextClick() {
        viewModelScope.launch {
            val heightNumber = height.toIntOrNull() ?: kotlin.run {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(
                        UiText.StringResource(R.string.error_height_cant_be_empty)
                    )
                )
                return@launch
            }
            preferences.saveHeight(heightNumber)
            initHeight = heightNumber
            height = heightNumber.toString()
            _uiEvent.send(UiEvent.Success)
        }
    }
}