package com.realityexpander.animatedcountercompose.ui

import android.content.res.Configuration
import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.realityexpander.animatedcountercompose.R
import com.realityexpander.animatedcountercompose.ui.theme.AnimatedCounterComposeTheme
import kotlinx.coroutines.delay
import kotlin.random.Random


fun floatsToAndroidColor(r: Float, g: Float, b: Float, a: Float): Int {
    return android.graphics.Color.argb(
        (255 * a).toInt(),
        (255 * r).toInt(),
        (255 * g).toInt(),
        (255 * b).toInt()
    )
}

@RequiresApi(Build.VERSION_CODES.O)
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

            Box {


                // Shadow - Cloudy (text blur) only works WITHOUT AnimatedContent
//                Cloudy(radius = 12, key1 = char) {
//                    Text(
//                        text = char.toString(),
//                        style = style,
//                        softWrap = false,
//                        color = Color(.90f, .90f, .90f, 1.0f)
//                    )
//                }
//                Text(
//                    text = char.toString(),
//                    style = style,
//                    softWrap = false,
//                    color = Color(.50f, .50f, .99f, 1.0f)
//                )

                AnimatedContent(
                    targetState = char, // Only animates/recompose if `char` changes. This will also trigger a recomposition.
                    transitionSpec = {
                        fadeIn(tween(400)) with fadeOut(tween(400))
//                        slideInVertically(  // more complex-looking animated transition
//                            tween(300)
//                        ) { it } with
//                                slideOutVertically(
//                                    tween(300)
//                                ) { -it }
                    }
                ) { char ->

                    Text(
                        text = char.toString(),
                        style = style,
//                        style = style.copy(  // Only works in Android 12
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

                    // Shadow - Only works in Android 12
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

                AndroidView(
                    modifier = Modifier,
                    factory = { context ->
                        TextView(context).apply {
                            setText(char.toString())
                            textSize = 48.sp.value
                            this.typeface = resources.getFont(R.font.font16segments_regular)
//                            setTextColor(floatsToAndroidColor(0f, 0f, 0f, 1.0f)) // to show only shadow
                            setTextColor(floatsToAndroidColor(.50f, .50f, .99f, 1.0f))
//                            setShadowLayer(3.5f, 0f, 0f, context.getColor(R.color.white)) // uses resource for color.
                            setShadowLayer(15.5f, 0f, 0f, floatsToAndroidColor(0.6f, 0.6f, 1.0f, 1.0f))
                        }
                    },
                    update = {
                        it.setText(char.toString())
                    }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
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

fun asciiToChar(): String {
//    return "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ!\"#\$%&'()*+,-./:;?@[\\]{|}".map {
    return "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".map {
        it.code
    }.joinToString("") {
        it.toChar().toString()
    }
}

fun String.replaceCharsAtUnlockedIndexes(lockedIndexes: Array<Boolean>): String {
    if(this.isBlank()) return ""

    var result: String = ""

    this.forEachIndexed { index, char ->
        if(!lockedIndexes[index]) {
//            result += Char(Random.nextInt(33, 126))
            result += asciiToChar()[Random.nextInt(0, asciiToChar().length)]
        } else {
            result += char
        }
    }

    return result
}
fun main() {
    println(asciiToChar())
}
