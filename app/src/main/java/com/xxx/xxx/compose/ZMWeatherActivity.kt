package com.xxx.xxx.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.xxx.xxx.R
import com.xxx.xxx.compose.ui.theme.AProjectTheme

/**
 * 08.16
 * 1.练手
 */
class ZMWeatherActivity : ComponentActivity() {
    private var nowIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        Modifier.fillMaxSize(),
//                        Arrangement.Bottom//子布局底部对齐
                    ) {
                        Text("内容", Modifier.weight(1f))
                        ShowBottomMenu(0)
                    }
                }
            }
        }
    }

    @Composable
    fun ShowBottomMenu(index: Int) {
        Row(
            Modifier.fillMaxWidth(),
            Arrangement.SpaceBetween,
            ) {
            BottomMenuItem(
                0,
                R.mipmap.table_ic_weather_normal,
                "天气",
                if (index == 0) Color.Blue else Color.Gray
            )
            BottomMenuItem(
                1,
                R.mipmap.table_ic_calendar_normal,
                "90天",
                if (index == 1) Color.Blue else Color.Gray
            )
            BottomMenuItem(
                2,
                R.mipmap.table_ic_find_normal,
                "发现",
                if (index == 2) Color.Blue else Color.Gray
            )
            BottomMenuItem(
                3,
                R.mipmap.table_ic_me_normal,
                "我的",
                if (index == 3) Color.Blue else Color.Gray
            )
        }
    }


    @Composable
    fun BottomMenuItem(index: Int, icon: Int, title: String, tint: Color) {
        Column {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                tint = tint,
                modifier = Modifier.clickable {
//                    ClickShow(index)
                })
            Text(text = title)
        }
    }

    @Composable
    private fun ClickShow(index: Int) {
        nowIndex = index
        ShowBottomMenu(index = nowIndex)
    }
}