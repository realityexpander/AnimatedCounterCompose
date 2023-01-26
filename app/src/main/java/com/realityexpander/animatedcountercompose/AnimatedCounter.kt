@file:OptIn(ExperimentalAnimationApi::class)

package com.realityexpander.animatedcountercompose

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.realityexpander.animatedcountercompose.ui.theme.AnimatedCounterComposeTheme
import com.realityexpander.animatedcountercompose.ui.theme.OCRFont
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


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedTextFlipper(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.body1
) {
    var oldText by remember {
        mutableStateOf(text)
    }
    SideEffect {
        oldText = text
    }

    Row(modifier = modifier) {

        val textString = text
        val oldTextString = oldText.toString()

        // For each individual `char` in `countString`...
        for(i in textString.indices) {
            val currentChar = textString[i]
            val oldChar = oldTextString.getOrNull(i)

            // We want animate only if any `char` is different
            val char = if(oldChar == currentChar) {
                oldTextString[i]
            } else {
                textString[i]
            }

            AnimatedContent(
                targetState = char, // Only animate if `char` changes. This will also trigger a recomposition.
                transitionSpec = {
                    slideInVertically(
                        tween(100)
                    ) { it } with slideOutVertically(
                        tween(100)
                    ) { -it }
                }
            ) { char ->
                Text(
                    text = char.toString(),
                    style = style,
//                    style = style.copy(  // Only works in Android 12
//                        shadow = Shadow(
//                            color = Color(1.0f, 1.0f, 1.0f, .9f),
//                            offset = Offset(x = 2f, y = 4f),
//                            blurRadius = 3.5f
//                        )
//                    ),
                    softWrap = false,
                    color = Color(.50f, .50f, .99f, 1.0f)
                )

                // Dropshadow using classic views
                // https://medium.com/tech-takeaways/jetpack-compose-drop-shadow-text-effect-b2f95d0dc2b5

                // Only works in Android 12
//                Text(
//                    modifier = modifier
//                        .alpha(alpha = 0.95f)
//                        .offset(x = 1.dp, y = 2.dp)
//                        .blur(radius = 8.dp),
//                    color = colorResource(id = R.color.white),
//                    fontSize = 48.sp,
//                    style = style,
//                    text = char.toString(),
//                )
//                Text(
//                    modifier = modifier,
//                    color = Color(.50f, .50f, .99f, 1.0f),
//                    fontSize = 48.sp,
//                    style = style,
//                    text = char.toString(),
//                )
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


@Preview(showBackground = true, device = "id:Nexus One", backgroundColor = 0xFF1F1B1B,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun PreviewWargames() {

    var text by remember {
        mutableStateOf(get10CharAlphanumericString())
    }
    var lockedIndexes by remember {
        mutableStateOf(arrayOf(false, false, false, false, false, false, false, false, false, false))
    }

    // increment count every second
    LaunchedEffect(key1 = true) {
        delay(200)

        while(true) {
            text = text.replaceCharsAtUnlockedIndexes(lockedIndexes)

            delay(200)
        }
    }

    LaunchedEffect(key1 = true) {
        delay(1000)

        while(true) {
            lockedIndexes[Random.nextInt(0,10)] = true
            lockedIndexes = lockedIndexes.copyOf()

            delay(1500)
        }
    }

    AnimatedCounterComposeTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedTextFlipper(
                text = text,
                style = MaterialTheme.typography.h3,
            )
        }
    }
}

// function to generate string of 16 alphanumeric characters
fun get10CharAlphanumericString(): String  {
    return (0..9).map {
        val code = Random.nextInt(65, 65+26)

        Char(code)
    }.joinToString("") {
        it.toString()
    }
}

fun String.replaceCharsAtUnlockedIndexes(lockedIndexes: Array<Boolean>): String {
    if(this.isBlank()) return ""

    var result: String = ""

    this.forEachIndexed { index, char ->
        if(!lockedIndexes[index]) {
            result += Char(Random.nextInt(65, 65+26))
        } else {
            result += char
        }
    }

    return result
}



