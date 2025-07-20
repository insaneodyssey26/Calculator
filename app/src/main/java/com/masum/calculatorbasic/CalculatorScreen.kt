package com.masum.calculatorbasic


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.animation.Crossfade
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masum.calculatorbasic.ui.theme.*

@Composable
fun CalculatorScreen(
    state: States,
    buttonSpacing: Dp = 12.dp,
    modifier: Modifier = Modifier,
    onAction: (Actions) -> Unit
) {
    val resultScale by animateFloatAsState(
        targetValue = if (state.number1.isNotEmpty() || state.number2.isNotEmpty()) 1.0f else 0.95f,
        animationSpec = tween(durationMillis = 200),
        label = "result_scale"
    )
    
    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        DarkBackground,
                        MediumGray
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            HistoryPanel(
                history = state.history,
                isVisible = state.showHistory,
                onHistoryItemClick = { result ->
                    onAction(Actions.UseHistoryResult(result))
                },
                onClearHistory = {
                    onAction(Actions.ClearHistory)
                }
            )
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = DisplayBackground.copy(alpha = 0.8f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconButton(
                            onClick = { onAction(Actions.ToggleHistory) }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.History,
                                contentDescription = "History",
                                tint = if (state.history.isNotEmpty()) AccentBlue else DisplaySecondary
                            )
                        }
                    }
                    
                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.End
                    ) {
                        if (state.number1.isNotEmpty() && state.operation != null) {
                            Text(
                                text = "${state.number1} ${state.operation.symbol}",
                                fontSize = 20.sp,
                                color = DisplaySecondary,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        
                        Crossfade(
                            targetState = state.number1 + (state.operation?.symbol ?: "") + state.number2,
                            label = "calculator_display"
                        ) { displayText ->
                            Text(
                                text = if (displayText.isEmpty()) "0" else displayText,
                                fontSize = when {
                                    displayText.length > 8 -> 48.sp
                                    displayText.length > 6 -> 56.sp
                                    else -> 64.sp
                                },
                                fontWeight = FontWeight.Light,
                                color = DisplayText,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2
                            )
                        }
                    }
                }
            }
            
            Column(
                verticalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    Buttons(
                        symbol = "AC",
                        buttonType = ButtonType.FUNCTION,
                        modifier = Modifier
                            .weight(2f)
                            .aspectRatio(2f),
                        onClick = { onAction(Actions.Clear) }
                    )
                    Buttons(
                        symbol = "Del",
                        buttonType = ButtonType.FUNCTION,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        onClick = { onAction(Actions.Delete) }
                    )
                    Buttons(
                        symbol = "÷",
                        buttonType = ButtonType.OPERATOR,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        onClick = { onAction(Actions.Operation(Operations.Divide)) }
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    Buttons(
                        symbol = "7",
                        buttonType = ButtonType.NUMBER,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        onClick = { onAction(Actions.Number(7)) }
                    )
                    Buttons(
                        symbol = "8",
                        buttonType = ButtonType.NUMBER,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        onClick = { onAction(Actions.Number(8)) }
                    )
                    Buttons(
                        symbol = "9",
                        buttonType = ButtonType.NUMBER,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        onClick = { onAction(Actions.Number(9)) }
                    )
                    Buttons(
                        symbol = "×",
                        buttonType = ButtonType.OPERATOR,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        onClick = { onAction(Actions.Operation(Operations.Multiply)) }
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    Buttons(
                        symbol = "4",
                        buttonType = ButtonType.NUMBER,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        onClick = { onAction(Actions.Number(4)) }
                    )
                    Buttons(
                        symbol = "5",
                        buttonType = ButtonType.NUMBER,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        onClick = { onAction(Actions.Number(5)) }
                    )
                    Buttons(
                        symbol = "6",
                        buttonType = ButtonType.NUMBER,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        onClick = { onAction(Actions.Number(6)) }
                    )
                    Buttons(
                        symbol = "−",
                        buttonType = ButtonType.OPERATOR,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        onClick = { onAction(Actions.Operation(Operations.Subtract)) }
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    Buttons(
                        symbol = "1",
                        buttonType = ButtonType.NUMBER,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        onClick = { onAction(Actions.Number(1)) }
                    )
                    Buttons(
                        symbol = "2",
                        buttonType = ButtonType.NUMBER,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        onClick = { onAction(Actions.Number(2)) }
                    )
                    Buttons(
                        symbol = "3",
                        buttonType = ButtonType.NUMBER,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        onClick = { onAction(Actions.Number(3)) }
                    )
                    Buttons(
                        symbol = "+",
                        buttonType = ButtonType.OPERATOR,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        onClick = { onAction(Actions.Operation(Operations.Add)) }
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    Buttons(
                        symbol = "0",
                        buttonType = ButtonType.NUMBER,
                        modifier = Modifier
                            .weight(2f)
                            .aspectRatio(2f),
                        onClick = { onAction(Actions.Number(0)) }
                    )
                    Buttons(
                        symbol = ".",
                        buttonType = ButtonType.NUMBER,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        onClick = { onAction(Actions.Decimal) }
                    )
                    Buttons(
                        symbol = "=",
                        buttonType = ButtonType.OPERATOR,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        onClick = { onAction(Actions.Calculate) }
                    )
                }
            }
        }
    }
}
