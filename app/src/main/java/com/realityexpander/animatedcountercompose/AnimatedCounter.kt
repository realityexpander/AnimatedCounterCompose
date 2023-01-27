@file:OptIn(ExperimentalAnimationApi::class)

package com.realityexpander.animatedcountercompose

import android.content.res.Configuration
import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.realityexpander.animatedcountercompose.ui.theme.AnimatedCounterComposeTheme
import kotlinx.coroutines.delay
import kotlin.random.Random

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedCounter(
    count: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.body1
) {
    var oldCount by remember {
        mutableStateOf(count)
    }
    SideEffect {
        println("2 - SideEffect pre count: $count, oldCount: $oldCount")
        oldCount = count
    }

    Row(modifier = modifier) {
        println("1 - Row count: $count, oldCount: $oldCount")

        val countString = count.toString()
        val oldCountString = oldCount.toString()

        // For each individual `char` in `countString`...
        for(i in countString.indices) {
            val currentChar = countString[i]
            val oldChar = oldCountString.getOrNull(i)

            // We want animate only if any `char` is different
            val char = if(oldChar == currentChar) {
                    oldCountString[i]
                } else {
                    countString[i]
                }

            AnimatedContent(
                targetState = char, // Only animate if `char` changes. This will also trigger a recomposition.
                transitionSpec = {
                    slideInVertically { it } with fadeOut() + slideOutVertically { -it }
                }
            ) { char ->
                Text(
                    text = char.toString(),
                    style = style,
                    softWrap = false,
                    color = Color.Green
                )
            }
        }
    }
}

@Preview(showBackground = true, device = "id:Nexus One")
@Composable
fun PreviewAnimatedCounter() {

    // make a state variable to hold the count
    var count by remember {
        mutableStateOf(123)
    }
    // increment count every second
    LaunchedEffect(key1 = true) {
        delay(200)

        while(true) {
            count+= Random.nextInt(0,50)
            delay(500)
        }
    }

    AnimatedCounterComposeTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedCounter(count = count, style = MaterialTheme.typography.h2)
        }
    }
}



