package com.xxx.xxx.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.xxx.xxx.R

/**
 * 08.17
 * 设置点击切换按钮变化
 * 回调将点击的位置返回给上层
 */
@Composable
fun BottomMenuBar(index: Int, onSelectChange: (Int) -> Unit) {

    Row(
        Modifier.fillMaxWidth(),
        Arrangement.SpaceBetween,
    ) {
        BottomMenuItem(
            0,
            R.mipmap.table_ic_weather_normal,
            "天气",
            if (index == 0) Color.Yellow else Color.Gray,
            Modifier
                .weight(1f)
                .clickable {
                    onSelectChange(0)
                }
        )
        BottomMenuItem(
            1,
            R.mipmap.table_ic_calendar_normal,
            "90天",
            if (index == 1) Color.Yellow else Color.Gray,
            Modifier
                .weight(1f)
                .clickable {
                    onSelectChange(1)
                }
        )
        BottomMenuItem(
            2,
            R.mipmap.table_ic_find_normal,
            "发现",
            if (index == 2) Color.Yellow else Color.Gray,
            Modifier
                .weight(1f)
                .clickable {
                    onSelectChange(2)
                }
        )
        BottomMenuItem(
            3,
            R.mipmap.table_ic_me_normal,
            "我的",
            if (index == 3) Color.Yellow else Color.Gray,
            Modifier
                .weight(1f)
                .clickable {
                    onSelectChange(3)
                }
        )
    }
}

@Composable
fun BottomMenuItem(index: Int, icon: Int, title: String, tint: Color, modifier: Modifier) {
    Column(
        modifier.padding(vertical = 8.dp),
        //设置子布局水平居中
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = title,
            tint = tint
        )
        Text(text = title, color = tint)
    }
}