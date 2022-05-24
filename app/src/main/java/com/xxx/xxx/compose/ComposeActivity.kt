package com.xxx.xxx.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xxx.xxx.compose.ui.theme.AProjectTheme
import kotlinx.coroutines.delay

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //记录状态
            var isSplash by remember { mutableStateOf(true) }
            //是否闪屏页
            if (isSplash) {
                SplashPage { isSplash = false }
            } else {
                MainPage()
            }
        }
    }

}

@Composable
fun SplashPage(onNextPage: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), contentAlignment = Alignment.TopCenter
    ) {
        //1s后返回
        LaunchedEffect(Unit) {
            delay(1000)
            onNextPage.invoke()
        }
        Text(
            text = "Wan Android",
            fontSize = 32.sp,
            color = Color.Green,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(0.dp, 150.dp, 0.dp, 0.dp)
        )
    }
}

@Composable
fun MainPage() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), contentAlignment = Alignment.TopCenter
    ) {

        Text(
            text = "Main Activity",
            fontSize = 32.sp,
            color = Color.Blue,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(0.dp, 150.dp, 0.dp, 0.dp)
        )
    }
}

