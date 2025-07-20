package com.masum.calculatorbasic

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.masum.calculatorbasic.ui.theme.*

enum class ButtonType {
    NUMBER, OPERATOR, FUNCTION
}

@Composable
fun Buttons(
    symbol: String,
    modifier: Modifier,
    onClick: () -> Unit,
    buttonType: ButtonType = ButtonType.NUMBER
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val haptic = LocalHapticFeedback.current
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1.0f,
        animationSpec = tween(durationMillis = 100),
        label = "button_scale"
    )
    
    val (backgroundColor, textColor) = when (buttonType) {
        ButtonType.OPERATOR -> {
            if (isPressed) OrangePressed to Color.White
            else Orange to Color.White
        }
        ButtonType.FUNCTION -> {
            if (isPressed) FunctionButtonPressed to Color.Black
            else FunctionButton to Color.Black
        }
        ButtonType.NUMBER -> {
            if (isPressed) NumberButtonPressed to Color.White
            else NumberButton to Color.White
        }
    }
    
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .scale(scale)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            }
            .then(modifier)
    ) {
        Text(
            text = symbol,
            fontSize = when (symbol.length) {
                1 -> 32.sp
                2 -> 24.sp
                else -> 18.sp
            },
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}