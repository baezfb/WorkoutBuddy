package com.hbaez.onboarding_presentation.age

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
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class AgeViewModel @Inject constructor(
    private val preferences: Preferences,
    private val filterOutDigits: FilterOutDigits
): ViewModel() {

    var age by mutableStateOf(LocalDate.parse(LocalDate.now().toString()).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        if(preferences.loadUserInfo().age != -1L){
            age = preferences.loadUserInfo().age
        }
    }

    fun onAgeEnter(age: Long) {
        this.age = age + 86400000 // add 1 day. Not sure why
    }

    fun onNextClick() {
        viewModelScope.launch {
            val ageNumber = age
//                ?: kotlin.run {
//                _uiEvent.send(
//                    UiEvent.ShowSnackbar(
//                        UiText.StringResource(R.string.error_age_cant_be_empty)
//                    )
//                )
//                return@launch
//            }
            preferences.saveAge(ageNumber)
            _uiEvent.send(UiEvent.Success)
        }
    }
}