package com.masum.calculatorbasic

import android.service.credentials.Action
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masum.calculatorbasic.ui.theme.LightGray

@Composable
fun CalculatorScreen(
    states: States,
    modifier: Modifier = Modifier,
    onAction: (Actions) -> Unit
    ) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = states.number1 + (states.operation?.symbol?: "") + states.number2,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp),
                fontSize = 80.sp,
                fontStyle = FontStyle.Normal,
                color = Color.White,
                maxLines = 2
            )
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Buttons(
                    symbol = "AC",
                    modifier = Modifier
                        .background(LightGray)
                        .weight(2f)
                        .aspectRatio(2f),
                    onClick = {
                        onAction(Actions.clear)
                    }
                )
            }
        }
    }
}

