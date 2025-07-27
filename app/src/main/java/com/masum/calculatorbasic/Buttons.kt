package com.masum.calculatorbasic

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.indication
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
        targetValue = if (isPressed) 0.92f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "button_scale"
    )
    
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 2.dp else 8.dp,
        animationSpec = tween(durationMillis = 150),
        label = "button_elevation"
    )
    
    val flashColor by animateColorAsState(
        targetValue = if (isPressed) Color.White.copy(alpha = 0.18f) else Color.Transparent,
        animationSpec = tween(durationMillis = 80), label = "flash_color"
    )
    val popScale by animateFloatAsState(
        targetValue = if (isPressed) 0.86f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ), label = "pop_scale"
    )
    val popElevation by animateDpAsState(
        targetValue = if (isPressed) 0.dp else 12.dp,
        animationSpec = tween(durationMillis = 120), label = "pop_elevation"
    )
    
    val (backgroundColor, textColor, pressedColor) = when (buttonType) {
        ButtonType.OPERATOR -> {
            Triple(Orange, Color.White, OrangePressed)
        }
        ButtonType.FUNCTION -> {
            Triple(FunctionButton, Color.White, FunctionButtonPressed)
        }
        ButtonType.NUMBER -> {
            Triple(NumberButton, Color.White, NumberButtonPressed)
        }
    }
    
    val currentBackgroundColor by animateColorAsState(
        targetValue = if (isPressed) pressedColor else backgroundColor,
        label = "background_color"
    )
    
    val finalBackgroundColor by animateColorAsState(
        targetValue = if (isPressed) pressedColor.copy(alpha = 0.8f) else backgroundColor,
        label = "final_background_color"
    )
    
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .scale(popScale)
            .shadow(
                elevation = popElevation,
                shape = CircleShape,
                ambientColor = when (buttonType) {
                    ButtonType.OPERATOR -> OrangeGlow
                    ButtonType.FUNCTION -> FunctionButtonGlow
                    ButtonType.NUMBER -> NumberButtonGlow
                }
            )
            .clip(CircleShape)
            .background(finalBackgroundColor)
            .background(flashColor)
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
                1 -> 28.sp
                2 -> 22.sp
                3 -> 18.sp
                else -> 16.sp
            },
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}