@file:OptIn(ExperimentalAnimationApi::class)

package com.realityexpander.animatedcountercompose

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay

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
            val oldChar = oldCountString.getOrNull(i)
            val newChar = countString[i]

            // Only animate if any `char` is different
            val char = if(oldChar == newChar) {
                    oldCountString[i]
                } else {
                    countString[i]
                }

            AnimatedContent(
                targetState = char, // Only animate if `char` changes. This will also trigger a recomposition.
                transitionSpec = {
                    slideInVertically { it } with slideOutVertically { -it }
                }
            ) { char ->
                Text(
                    text = char.toString(),
                    style = style,
                    softWrap = false
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
        while(true) {
            count++
            delay(1000)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedCounter(count = count, style = MaterialTheme.typography.h2)
    }
}