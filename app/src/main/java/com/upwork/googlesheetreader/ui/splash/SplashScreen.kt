package com.upwork.googlesheetreader.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.upwork.googlesheetreader.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(modifier: Modifier, navigateToHome: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(5000L)
        navigateToHome.invoke()
    }
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Image(painter = painterResource(id = R.drawable.logo_splash),
            contentDescription = "",
            modifier=modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight)
    }
}

@Composable
fun Loader() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.start))
    LottieAnimation(composition)
}