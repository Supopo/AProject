package com.xxx.xxx.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.xxx.xxx.R
import com.xxx.xxx.compose.ui.theme.AProjectTheme

/**
 * 08.16
 * 1.练手
 */
class ZMWeatherActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //状态监听
        var nowIndex by mutableStateOf(0)
        setContent {
            AProjectTheme(nowIndex == 0 || nowIndex == 2) {
                // A surface container using the 'background' color from the theme
                Surface(
                    Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        Modifier.fillMaxSize(),
                    ) {
                        LazyColumn(Modifier.weight(1f)) {
                            items(5) { index ->
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 5.dp, horizontal = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.mipmap.ic_logo),
                                        contentDescription = "",
                                        Modifier
                                            .size(60.dp)
                                            .padding(end = 20.dp)
                                    )
                                    Text(
                                        text = "巴拉巴拉小魔仙~", modifier = Modifier
                                            .weight(1f),
                                        //文字内部居中
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                        BottomMenuBar(nowIndex) {
                            nowIndex = it
                        }
                    }
                }
            }
        }
    }
}

