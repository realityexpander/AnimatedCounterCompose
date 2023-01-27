package com.realityexpander.animatedcountercompose

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.realityexpander.animatedcountercompose.ui.AnimatedTextFlipper
import com.realityexpander.animatedcountercompose.ui.get10CharAlphanumericString
import com.realityexpander.animatedcountercompose.ui.replaceCharsAtUnlockedIndexes
import com.realityexpander.animatedcountercompose.ui.theme.AnimatedCounterComposeTheme
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimatedCounterComposeTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // For manual counter
                    var count by remember {
                        mutableStateOf(0)
                    }

                    // Driver For WarGames
                    var text by remember {
                        mutableStateOf(get10CharAlphanumericString())
                    }
                    var lockedIndexes by remember {
                        mutableStateOf(Array<Boolean>(10) { false })
                    }
                    LaunchedEffect(key1 = true) {
                        delay(200)

                        while(true) {
                            text = text.replaceCharsAtUnlockedIndexes(lockedIndexes)

                            delay(250)
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

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        AnimatedCounter(
                            count = count,
                            style = MaterialTheme.typography.h1
                        )
                        Button(onClick = {
                            count++
                            lockedIndexes = Array<Boolean>(10) { false }
                        }) {
                            Text(text = "Increment")
                        }

                        Spacer(modifier = Modifier.height(30.dp))
                        Divider(modifier = Modifier.height(4.dp))
                        Spacer(modifier = Modifier.height(30.dp))

                        AnimatedTextFlipper(
                            text = text,
                            style = MaterialTheme.typography.h3,
                        )
                    }

                }
            }
        }
    }
}