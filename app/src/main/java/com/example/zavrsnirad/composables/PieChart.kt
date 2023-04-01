package com.example.zavrsnirad.composables

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zavrsnirad.transactionsIndex

@Composable
fun PieChart(
    data: Map<String, Int>,
    radiusOuter: Dp = 90.dp,
    chartBarWidth: Dp = 25.dp,
    animDuration: Int = 500,
    ) {

    val totalSum = data.values.sum()
    val floatValue = mutableListOf<Float>()

    data.values.forEachIndexed { index, values ->
        floatValue.add(index, 360 * values.toFloat() / totalSum.toFloat())
    }

    var animationPlayed by remember { mutableStateOf(false)}

    var lastValue = 0f

    val animateSize by animateFloatAsState(
        targetValue = if (animationPlayed) radiusOuter.value * 2f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        )
    )

    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 90f * 11f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        )
    )

    LaunchedEffect(key1 = true){
        animationPlayed = true
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val colorsForText = mutableListOf<Color>()

        Box(
            modifier = Modifier.size(animateSize.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(radiusOuter * 2f)
                    .rotate(animateRotation)
            ) {
                floatValue.forEachIndexed { index, value ->

                    var categoryColor: Color = Color.Blue;

                    val targetCategory = data.keys.elementAt(index)

                    transactionsIndex.forEach{ type ->
                        if(type.name == targetCategory){
                            categoryColor = type.textBackgroundColor
                        }
                    }

                    colorsForText.add(categoryColor)

                    drawArc(
                        color = categoryColor,
                        lastValue,
                        value,
                        useCenter = false,
                        style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Butt)
                    )
                    lastValue += value
                }
            }
        }

        DetailsPieChart(
            data = data
        )
    }
}

@Composable
fun DetailsPieChart(
    data: Map<String, Int>,
) {
    val providedColors = mutableListOf<Color>()

    data.forEach { s, i ->
        var categoryColor: Color = Color.Blue;
        val targetCategory = s

        transactionsIndex.forEach{ type ->
            if(type.name == targetCategory){
                categoryColor = type.textBackgroundColor
            }
        }

        providedColors.add(categoryColor)
    }

    Column(
        modifier = Modifier
            .padding(top = 80.dp)
            .fillMaxWidth()
    ) {
        data.values.forEachIndexed { index, value ->

            //Log.d("****DEBUG****", index.toString())
            //Log.d("****DEBUG****", providedColors.toString())

            DetailsPieChartItem(
                data = Pair(data.keys.elementAt(index), value),
                color = providedColors[index],
            )

        }

    }
}

@Composable
fun DetailsPieChartItem(
    data: Pair<String, Int>,
    height: Dp = 45.dp,
    color: Color
) {

    Surface(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 40.dp),
        color = Color.Transparent
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .background(
                        color = color,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .size(height)
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = data.first,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.Black
                )
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = (data.second.toInt()).toString() + "€",
                    fontWeight = FontWeight.Medium,
                    fontSize = 22.sp,
                    color = Color.Gray
                )
            }
        }
    }
}