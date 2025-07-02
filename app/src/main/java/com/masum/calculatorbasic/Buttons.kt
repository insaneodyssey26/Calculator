package com.masum.calculatorbasic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun Buttons (
    symbol: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(CircleShape)
            .clickable {
                onClick ()
            }
            .then(modifier)
    ){
        Text(
            text = symbol,
            fontSize = 36.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}