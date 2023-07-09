package com.example.workout_logger_presentation.components

import androidx.compose.material.AlertDialog
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import coil.annotation.ExperimentalCoilApi
import com.example.workout_logger_presentation.search_exercise.TrackableExerciseState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import com.example.workout_logger_presentation.create_exercise.model.Muscle
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core.R

@ExperimentalCoilApi
@Composable
fun ExerciseInfoDialog(
    trackableExerciseState: TrackableExerciseState,
    onDescrClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val exercise = trackableExerciseState.exercise
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    val colorMatrix = floatArrayOf(
        -1f, 0f, 0f, 0f, 255f,
        0f, -1f, 0f, 0f, 255f,
        0f, 0f, -1f, 0f, 255f,
        0f, 0f, 0f, 1f, 0f
    ) // inverts color

    val muscles: List<Muscle> = listOf(
        Muscle("Anterior deltoid", "/static/images/muscles/main/muscle-2.svg", true),
        Muscle("Biceps brachii", "/static/images/muscles/main/muscle-1.svg", true),
        Muscle("Biceps femoris", "/static/images/muscles/main/muscle-11.svg", false),
        Muscle("Brachialis", "/static/images/muscles/main/muscle-13.svg", true),
        Muscle("Gastrocnemius", "/static/images/muscles/main/muscle-7.svg", false),
        Muscle("Gluteus maximus", "/static/images/muscles/main/muscle-8.svg", false),
        Muscle("Latissimus dorsi", "/static/images/muscles/main/muscle-12.svg", false),
        Muscle("Obliquus externus abdominis", "/static/images/muscles/main/muscle-14.svg", true),
        Muscle("Pectoralis major", "/static/images/muscles/main/muscle-4.svg", true),
        Muscle("Quadriceps femoris", "/static/images/muscles/main/muscle-10.svg", true),
        Muscle("Rectus abdominis", "/static/images/muscles/main/muscle-6.svg", true),
        Muscle("Serratus anterior", "/static/images/muscles/main/muscle-3.svg", true),
        Muscle("Soleus", "/static/images/muscles/main/muscle-15.svg", false),
        Muscle("Trapezius", "/static/images/muscles/main/muscle-9.svg", false),
        Muscle("Triceps brachii", "/static/images/muscles/main/muscle-5.svg", false)
    )

    AlertDialog(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 500.dp)
            .wrapContentHeight(align = Alignment.CenterVertically)
            .clip( RoundedCornerShape(50.dp) ),
        onDismissRequest = { onDismiss() },
        title = null,
        text = null,
        buttons = {
            Column(
                modifier = modifier
                    .clip(RoundedCornerShape(5.dp))
                    .padding(spacing.spaceSmall)
                    .shadow(
                        elevation = 1.dp,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .background(MaterialTheme.colors.surface)
                    .verticalScroll(enabled = true, state = rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = rememberImagePainter(
                            data = if (trackableExerciseState.exercise.image_url.isNotEmpty()) trackableExerciseState.exercise.image_url[0] else "",
                            builder = {
                                crossfade(true)
                                error(R.drawable.ic_exercise)
                                fallback(R.drawable.ic_exercise)
                            }
                        ),
                        contentDescription = exercise.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(75.dp)
                            .clip(RoundedCornerShape(topStart = 5.dp)),
                        colorFilter = ColorFilter.colorMatrix(ColorMatrix(colorMatrix))
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceSmall))
                    Text(
                        text = exercise.name!!,
                        style = MaterialTheme.typography.body1,
                        maxLines = 2,
                        overflow = TextOverflow.Visible,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(spacing.spaceMedium),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Column {
                        Text(
                            text = stringResource(id = R.string.descr),
                            style = MaterialTheme.typography.body2,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .height(IntrinsicSize.Max)
                        )
                        Text(
                            text = exercise.description ?: "N/A",
                            style = MaterialTheme.typography.body2,
                            maxLines = if(trackableExerciseState.isDescrExpanded) 100 else 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .height(IntrinsicSize.Max)
                                .clickable {
                                    onDescrClick()
                                }
                        )
                        if(exercise.image_url.filterNotNull().isNotEmpty() && (exercise.image_url.size == 1 && !exercise.image_url[0].equals("null"))){
                            Spacer(modifier = Modifier.height(spacing.spaceMedium))
                            Text(
                                text = stringResource(id = R.string.exercise_demonstration),
                                style = MaterialTheme.typography.body2,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .height(IntrinsicSize.Max)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                exercise.image_url.forEach {
                                    Image(
                                        painter = rememberImagePainter(
                                            data = it,
                                            builder = {
                                                crossfade(true)
                                                error(R.drawable.ic_exercise)
                                                fallback(R.drawable.ic_exercise)
                                            }
                                        ),
                                        contentDescription = exercise.name,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .size(100.dp)
                                            .weight(1f)
                                            .clip(RoundedCornerShape(topStart = 5.dp)),
                                        colorFilter = ColorFilter.colorMatrix(ColorMatrix(colorMatrix))
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(spacing.spaceMedium))
                        Text(
                            text = stringResource(id = R.string.exercise_muscle),
                            style = MaterialTheme.typography.body2,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .height(IntrinsicSize.Max)
                        )
                        Text(
                            text = exercise.muscle_name_main ?: "N/A",
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier
                                .height(IntrinsicSize.Max)
                        )
                        Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
                        Text(
                            text = stringResource(id = R.string.exercise_muscle_secondary),
                            style = MaterialTheme.typography.body2,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .height(IntrinsicSize.Max)
                        )
                        Text(
                            text = exercise.muscle_name_secondary ?: "N/A",
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier
                                .height(IntrinsicSize.Max)
                        )
                        Spacer(modifier = Modifier.height(spacing.spaceMedium))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)
                        ){
                            Log.println(Log.DEBUG, "image urls", exercise.image_url_main.toString())
                            Log.println(Log.DEBUG, "image urls", exercise.image_url_secondary.toString())
                            Box(
                                modifier = Modifier.fillMaxWidth(.5f),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_muscular_system_front), //else { painterResource(id = R.drawable.ic_muscular_system_back) },
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .size(200.dp)
                                        .clip(RoundedCornerShape(topStart = 5.dp))
                                )
                                if(exercise.image_url_main.isNotEmpty()){
                                    Log.println(Log.DEBUG, "image urls", "first if")
                                    exercise.image_url_main.forEach { image_url ->
                                        if(muscles.find{ it.imageURL == image_url }!!.isFront){
                                            Log.println(Log.DEBUG, "image urls", "second if")
                                            Image(
                                                painter = rememberImagePainter(
                                                    data = "https://wger.de${image_url!!.trim()}",
                                                    builder = {
                                                        crossfade(true)
                                                        decoder(SvgDecoder(context = context))
                                                    }
                                                ),
                                                contentDescription = image_url ?: exercise.name,
                                                contentScale = ContentScale.Fit,
                                                modifier = Modifier
                                                    .size(200.dp)
                                                    .clip(RoundedCornerShape(topStart = 5.dp))
                                            )
                                        }
                                    }
                                }
                                if(exercise.image_url_secondary.isNotEmpty()){
                                    Log.println(Log.DEBUG, "image urls", "first if")
                                    exercise.image_url_secondary.onEach { image_url ->
                                        if(muscles.find{ it.imageURL.replace("main","secondary") == image_url || it.imageURL == image_url}!!.isFront){
                                            Log.println(Log.DEBUG, "image urls", "second if")
                                            Image(
                                                painter = rememberImagePainter(
                                                    data = "https://wger.de${image_url!!.trim()}",
                                                    builder = {
                                                        crossfade(true)
                                                        decoder(SvgDecoder(context = context))
                                                    }
                                                ),
                                                contentDescription = image_url ?: exercise.name,
                                                contentScale = ContentScale.Fit,
                                                modifier = Modifier
                                                    .size(200.dp)
                                                    .clip(RoundedCornerShape(topStart = 5.dp))
                                            )
                                        }
                                    }
                                }
                            }
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_muscular_system_back), //else { painterResource(id = R.drawable.ic_muscular_system_back) },
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .size(200.dp)
                                        .clip(RoundedCornerShape(topStart = 5.dp))
                                )
                                if(exercise.image_url_main.isNotEmpty()){
                                    Log.println(Log.DEBUG, "image urls", "first if")
                                    exercise.image_url_main.onEach { image_url ->
                                        if(!muscles.find{ it.imageURL == image_url }!!.isFront){
                                            Log.println(Log.DEBUG, "image urls", "second if")
                                            Image(
                                                painter = rememberImagePainter(
                                                    data = "https://wger.de${image_url!!.trim()}",
                                                    builder = {
                                                        crossfade(true)
                                                        decoder(SvgDecoder(context = context))
                                                    }
                                                ),
                                                contentDescription = image_url ?: exercise.name,
                                                contentScale = ContentScale.Fit,
                                                modifier = Modifier
                                                    .size(200.dp)
                                                    .clip(RoundedCornerShape(topStart = 5.dp))
                                            )
                                        }
                                    }
                                }
                                if(exercise.image_url_secondary.isNotEmpty()){
                                    Log.println(Log.DEBUG, "image urls", "first if")
                                    exercise.image_url_secondary.onEach { image_url ->
                                        if(!muscles.find{ it.imageURL.replace("main","secondary") == image_url  || it.imageURL == image_url }!!.isFront ){
                                            Log.println(Log.DEBUG, "image urls", "second if")
                                            Image(
                                                painter = rememberImagePainter(
                                                    data = "https://wger.de${image_url!!.trim()}",
                                                    builder = {
                                                        crossfade(true)
                                                        decoder(SvgDecoder(context = context))
                                                    }
                                                ),
                                                contentDescription = image_url ?: exercise.name,
                                                contentScale = ContentScale.Fit,
                                                modifier = Modifier
                                                    .size(200.dp)
                                                    .clip(RoundedCornerShape(topStart = 5.dp))
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}