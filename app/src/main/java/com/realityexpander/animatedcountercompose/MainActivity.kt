package com.realityexpander.animatedcountercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.realityexpander.animatedcountercompose.ui.theme.AnimatedCounterComposeTheme
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimatedCounterComposeTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var count by remember {
                        mutableStateOf(0)
                    }

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
                        Button(onClick = { count++ }) {
                            Text(text = "Increment")
                        }

                        Divider(modifier = Modifier.height(20.dp))

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