package com.hbaez.tracker_presentation.search.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core.R
import com.hbaez.tracker_presentation.components.NutrientInfo
import com.hbaez.tracker_presentation.search.TrackableFoodUiState

@Composable
fun TrackableFoodItem(
    trackableFoodUiState: TrackableFoodUiState,
    onClick: () -> Unit,
    onAmountChange: (String) -> Unit,
    onTrack: () -> Unit,
    modifier: Modifier = Modifier
){
    val food = trackableFoodUiState.food
    val spacing = LocalSpacing.current
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .padding(spacing.spaceExtraSmall)
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(5.dp)
            )
            .background(MaterialTheme.colorScheme.inversePrimary)
            .clickable { onClick() }
            .padding(end = spacing.spaceMedium)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = food.image,
                        builder = {
                            crossfade(true)
                            error(R.drawable.ic_burger)
                            fallback(R.drawable.ic_burger)
                        }
                    ),
                    contentDescription = food.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(topStart = 5.dp))
                )
            }
            Spacer(modifier = Modifier.width(spacing.spaceSmall))
            Column(
                modifier = Modifier
                    .weight(2f)
            ){
                Row{
                    Text(
                        text = food.name,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.width(spacing.spaceSmall))
                Row {
                    NutrientInfo(
                        name = stringResource(id = R.string.kcal),
                        amount = food.calories,
//                    unit = stringResource(id = R.string.kcal_per_100g),
                        amountTextSize = 16.sp,
                        unitTextSize = 12.sp,
                        nameTextStyle = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceSmall))
                    NutrientInfo(
                        name = stringResource(id = R.string.carbs),
                        amount = food.carbs,
                        unit = stringResource(id = R.string.grams),
                        amountTextSize = 16.sp,
                        unitTextSize = 12.sp,
                        nameTextStyle = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceSmall))
                    NutrientInfo(
                        name = stringResource(id = R.string.protein),
                        amount = food.protein,
                        unit = stringResource(id = R.string.grams),
                        amountTextSize = 16.sp,
                        unitTextSize = 12.sp,
                        nameTextStyle = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceSmall))
                    NutrientInfo(
                        name = stringResource(id = R.string.fat),
                        amount = food.fat,
                        unit = stringResource(id = R.string.grams),
                        amountTextSize = 16.sp,
                        unitTextSize = 12.sp,
                        nameTextStyle = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.width(spacing.spaceSmall))
                Row {
                    Text(
                        text = food.quantity,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        AnimatedVisibility(visible = trackableFoodUiState.isExpanded) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.spaceMedium),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    BasicTextField(
                        value = trackableFoodUiState.amount,
                        textStyle = LocalTextStyle.current.copy(color = Color.White),
                        onValueChange = onAmountChange,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = if(trackableFoodUiState.amount.isNotBlank()) {
                                ImeAction.Done
                            } else ImeAction.Default
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                onTrack()
                                defaultKeyboardAction(ImeAction.Done)
                            }
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .border(
                                shape = RoundedCornerShape(5.dp),
                                width = 0.5.dp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            .alignBy(LastBaseline)
                            .padding(spacing.spaceMedium)
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceExtraSmall))
                    Text(
                        text = stringResource(id = R.string.servings),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.alignBy(LastBaseline)
                    )
                }
                IconButton(onClick = onTrack) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(id = R.string.track)
                    )
                }
            }
        }
    }
}