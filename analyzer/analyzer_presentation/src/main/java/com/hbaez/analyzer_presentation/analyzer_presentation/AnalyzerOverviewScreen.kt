package com.hbaez.analyzer_presentation.analyzer_presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi

@ExperimentalCoilApi
@Composable
fun AnalyzerOverviewScreen(
    viewModel: AnalyzerOverviewModel = hiltViewModel()
){
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Analyzer placeholder", textAlign = TextAlign.Center)
    }
}