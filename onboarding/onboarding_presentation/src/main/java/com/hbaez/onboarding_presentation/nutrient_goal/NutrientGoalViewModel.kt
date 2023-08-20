package com.hbaez.onboarding_presentation.nutrient_goal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hbaez.core.domain.model.ActivityLevel
import com.hbaez.core.domain.model.Gender
import com.hbaez.core.domain.model.GoalType
import com.hbaez.core.domain.model.UserInfo
import com.hbaez.core.domain.use_case.FilterOutDigits
import com.hbaez.core.util.UiEvent
import com.hbaez.onboarding_domain.use_case.ValidateNutrients
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.user_auth_presentation.model.service.AccountService
import com.hbaez.user_auth_presentation.model.service.ConfigurationService
import com.hbaez.user_auth_presentation.model.service.StorageService
import javax.inject.Inject

@HiltViewModel
class NutrientGoalViewModel @Inject constructor(
    private val preferences: Preferences,
    private val filterOutDigits: FilterOutDigits,
    private val validateNutrients: ValidateNutrients,
    private val storageService: StorageService,
    private val accountService: AccountService,
    private val configurationService: ConfigurationService
): ViewModel() {

    var state by mutableStateOf(NutrientGoalState())
        private set

    private val _uiEvent = Channel<UiEvent> {  }
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        if(preferences.loadUserInfo().carbRatio != -1F){
            state = state.copy(
                carbsRatio = (preferences.loadUserInfo().carbRatio * 100).toInt().toString(),
                proteinRatio = (preferences.loadUserInfo().proteinRatio * 100).toInt().toString(),
                fatRatio = (preferences.loadUserInfo().fatRatio * 100).toInt().toString()
            )
        }
    }

    fun onEvent(event: NutrientGoalEvent) {
        when(event) {
            is NutrientGoalEvent.OnCarbRatioEnter -> {
                state = state.copy(
                    carbsRatio = filterOutDigits(event.ratio)
                )
            }
            is NutrientGoalEvent.OnProteinRatioEnter -> {
                state = state.copy(
                    proteinRatio = filterOutDigits(event.ratio)
                )
            }
            is NutrientGoalEvent.OnFatRatioEnter -> {
                state = state.copy(
                    fatRatio = filterOutDigits(event.ratio)
                )
            }
            is NutrientGoalEvent.OnNextClick -> {
                val result = validateNutrients(
                    carbsRatioText = state.carbsRatio,
                    proteinsRatioText = state.proteinRatio,
                    fatRatioText = state.fatRatio
                )
                when(result) {
                    is ValidateNutrients.Result.Success -> {
                        preferences.saveCarbRatio(result.carbsRatio)
                        preferences.saveProteinRatio(result.proteinRatio)
                        preferences.saveFatRatio(result.fatRatio)
                        var userInfo = preferences.loadUserInfo()

                        viewModelScope.launch {
                            storageService.saveUserInfo(userInfo)
                            _uiEvent.send(UiEvent.Success)
                        }
                    }
                    is ValidateNutrients.Result.Error -> {
                        viewModelScope.launch {
                            _uiEvent.send(UiEvent.ShowSnackbar(result.message))
                        }
                    }
                }
            }

        }
    }
}