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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masum.calculatorbasic.ui.theme.LightGray
import com.masum.calculatorbasic.ui.theme.MediumGray
import com.masum.calculatorbasic.ui.theme.Orange

@Composable
fun CalculatorScreen(
    state: States,
    buttonSpacing: Dp = 8.dp,
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
            verticalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            Text(
                text = state.number1 + (state.operation?.symbol?: "") + state.number2,
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
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                Buttons(
                    symbol = "AC",
                    modifier = Modifier
                        .background(LightGray)
                        .weight(2f)
                        .aspectRatio(2f),
                    onClick = {
                        onAction(Actions.Clear)
                    }
                )
                Buttons(
                    symbol = "Del",
                    modifier = Modifier
                        .background(LightGray)
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = {
                        onAction(Actions.Delete)
                    }
                )
                Buttons(
                    symbol = "/",
                    modifier = Modifier
                        .background(Orange)
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = {
                        onAction(Actions.Operation(Operations.Divide))
                    }
                )
            }
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ){
                Buttons(
                    symbol = "7",
                    modifier = Modifier
                        .background(MediumGray)
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = {
                        onAction(Actions.Number(7))
                    }
                )
                Buttons(
                    symbol = "8",
                    modifier = Modifier
                        .background(MediumGray)
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = {
                        onAction(Actions.Number(8))
                    }
                )
                Buttons(
                    symbol = "9",
                    modifier = Modifier
                        .background(MediumGray)
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = {
                        onAction(Actions.Number(9))
                    }
                )
                Buttons(
                    symbol = "X",
                    modifier = Modifier
                        .background(Orange)
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = {
                        onAction(Actions.Operation(Operations.Multiply))
                    }
                )
            }
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ){
                Buttons(
                    symbol = "4",
                    modifier = Modifier
                        .background(MediumGray)
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = {
                        onAction(Actions.Number(4))
                    }
                )
                Buttons(
                    symbol = "5",
                    modifier = Modifier
                        .background(MediumGray)
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = {
                        onAction(Actions.Number(5))
                    }
                )
                Buttons(
                    symbol = "6",
                    modifier = Modifier
                        .background(MediumGray)
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = {
                        onAction(Actions.Number(6))
                    }
                )
                Buttons(
                    symbol = "-",
                    modifier = Modifier
                        .background(Orange)
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = {
                        onAction(Actions.Operation(Operations.Subtract))
                    }
                )
            }
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ){
                Buttons(
                    symbol = "1",
                    modifier = Modifier
                        .background(MediumGray)
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = {
                        onAction(Actions.Number(1))
                    }
                )
                Buttons(
                    symbol = "2",
                    modifier = Modifier
                        .background(MediumGray)
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = {
                        onAction(Actions.Number(2))
                    }
                )
                Buttons(
                    symbol = "3",
                    modifier = Modifier
                        .background(MediumGray)
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = {
                        onAction(Actions.Number(3))
                    }
                )
                Buttons(
                    symbol = "+",
                    modifier = Modifier
                        .background(Orange)
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = {
                        onAction(Actions.Operation(Operations.Add))
                    }
                )
            }
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ){
                Buttons(
                    symbol = "0",
                    modifier = Modifier
                        .background(MediumGray)
                        .weight(2f)
                        .aspectRatio(2f),
                    onClick = {
                        onAction(Actions.Number(0))
                    }
                )
                Buttons(
                    symbol = ".",
                    modifier = Modifier
                        .background(MediumGray)
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = {
                        onAction(Actions.Decimal)
                    }
                )
                Buttons(
                    symbol = "=",
                    modifier = Modifier
                        .background(Orange)
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = {
                        onAction(Actions.Calculate)
                    }
                )
            }
        }
    }
}

