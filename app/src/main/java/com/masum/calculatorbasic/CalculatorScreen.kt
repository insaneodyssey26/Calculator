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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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

@OptIn(ExperimentalLayoutApi::class)
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
                },
                onDeleteHistoryItem = { item ->
                    onAction(Actions.DeleteHistoryItem(item))
                },
                onClose = {
                    onAction(Actions.ToggleHistory)
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
                        val expression = buildString {
                            append(state.number1)
                            if (state.operation != null) append(" ${state.operation.symbol}")
                        }
                        if (expression.isNotBlank()) {
                            Text(
                                text = expression,
                                fontSize = 20.sp,
                                color = DisplaySecondary,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 1
                            )
                        }
                        val mainDisplay = when {
                            state.number2.isNotEmpty() -> state.number2
                            else -> state.number1
                        }
                        Crossfade(
                            targetState = mainDisplay,
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
            
            var scientificMenuExpanded by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                // Dropdown button for scientific operations
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(
                        onClick = { scientificMenuExpanded = true },
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(FunctionButton)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Scientific operations",
                            tint = Color.Black,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = scientificMenuExpanded,
                        onDismissRequest = { scientificMenuExpanded = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text("% (Percent)") },
                            onClick = {
                                onAction(Actions.UnaryOperation(Operations.Percent))
                                scientificMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("± (Plus/Minus)") },
                            onClick = {
                                onAction(Actions.UnaryOperation(Operations.PlusMinus))
                                scientificMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("√ (Square Root)") },
                            onClick = {
                                onAction(Actions.UnaryOperation(Operations.Sqrt))
                                scientificMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("x² (Square)") },
                            onClick = {
                                onAction(Actions.UnaryOperation(Operations.Square))
                                scientificMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("1/x (Reciprocal)") },
                            onClick = {
                                onAction(Actions.UnaryOperation(Operations.Reciprocal))
                                scientificMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("sin") },
                            onClick = {
                                onAction(Actions.UnaryOperation(Operations.Sin))
                                scientificMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("cos") },
                            onClick = {
                                onAction(Actions.UnaryOperation(Operations.Cos))
                                scientificMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("tan") },
                            onClick = {
                                onAction(Actions.UnaryOperation(Operations.Tan))
                                scientificMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("ln") },
                            onClick = {
                                onAction(Actions.UnaryOperation(Operations.Ln))
                                scientificMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("log") },
                            onClick = {
                                onAction(Actions.UnaryOperation(Operations.Log))
                                scientificMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("! (Factorial)") },
                            onClick = {
                                onAction(Actions.UnaryOperation(Operations.Factorial))
                                scientificMenuExpanded = false
                            }
                        )
                    }
                }
                Buttons(
                    symbol = "AC",
                    buttonType = ButtonType.FUNCTION,
                    modifier = Modifier
                        .weight(1.5f)
                        .aspectRatio(1.5f),
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
            Spacer(modifier = Modifier.height(buttonSpacing))
            Column(
                verticalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
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
