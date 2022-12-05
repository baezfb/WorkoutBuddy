package com.hbaez.onboarding_domain.use_case

import com.hbaez.core.util.UiText
import com.hbaez.core.R

class ValidateNutrients {

    operator fun invoke(
        carbsRatioText: String,
        proteinsRatioText: String,
        fatRatioText: String
    ): Result {
        val carbsRatio = carbsRatioText.toIntOrNull()
        val proteinRatio = proteinsRatioText.toIntOrNull()
        val fatRatio = fatRatioText.toIntOrNull()
        if(carbsRatio == null || proteinRatio == null || fatRatio == null) {
            return Result.Error(
                message = UiText.StringResource(R.string.error_invalid_values)
            )
        }
        if(carbsRatio + proteinRatio + fatRatio != 100) {
            return Result.Error(
                message = UiText.StringResource(R.string.error_not_100_percent)
            )
        }
        return Result.Success(
            carbsRatio / 100f,
            proteinRatio / 100f,
            fatRatio / 100f
        )
    }

    sealed class Result {
        data class Success(
            val carbsRatio: Float,
            val proteinRatio: Float,
            val fatRatio: Float
        ) : Result()
        data class Error(val message: UiText): Result()
    }
}