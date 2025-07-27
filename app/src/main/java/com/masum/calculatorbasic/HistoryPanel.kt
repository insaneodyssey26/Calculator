package com.masum.calculatorbasic

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masum.calculatorbasic.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryPanel(
    modifier: Modifier = Modifier,
    history: List<CalculationHistory>,
    isVisible: Boolean,
    onHistoryItemClick: (String) -> Unit,
    onClearHistory: () -> Unit,
    onDeleteHistoryItem: (CalculationHistory) -> Unit,
    onRestoreHistoryItem: (CalculationHistory, Int) -> Unit,
    onClose: () -> Unit = {}
) {
    var showSwipeAlert by remember { mutableStateOf(true) }
    var lastDeleted by remember { mutableStateOf<CalculationHistory?>(null) }
    var lastDeletedIndex by remember { mutableStateOf(-1) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current

    LaunchedEffect(isVisible) {
        if (isVisible && history.isNotEmpty() && showSwipeAlert) {
            delay(1000)
        }
    }

    Box {
        AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically { -it },
        exit = slideOutVertically { -it },
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
            colors = CardDefaults.cardColors(
                containerColor = DisplayBackground.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "History",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DisplayText
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onClearHistory,
                            enabled = history.isNotEmpty()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Clear History",
                                tint = if (history.isNotEmpty()) AccentRed else DisplaySecondary
                            )
                        }
                        IconButton(onClick = onClose) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close History",
                                tint = DisplaySecondary
                            )
                        }
                    }
                }
                
                HorizontalDivider(
                    color = DisplaySecondary.copy(alpha = 0.3f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                AnimatedVisibility(
                    visible = showSwipeAlert && history.isNotEmpty(),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = AccentBlue.copy(alpha = 0.2f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Info",
                                tint = AccentBlue,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Swipe right on any calculation to delete it",
                                color = DisplayText,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(
                                onClick = { showSwipeAlert = false }
                            ) {
                                Text(
                                    text = "Got it",
                                    color = AccentBlue,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                if (history.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No calculations yet",
                            color = DisplaySecondary,
                            fontSize = 16.sp
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(history, key = { "${it.expression}_${it.timestamp}" }) { item ->
                            AnimatedSwipeToDeleteHistoryItem(
                                item = item,
                                onClick = { onHistoryItemClick(item.result) },
                                onDelete = {
                                    lastDeleted = item
                                    lastDeletedIndex = history.indexOf(item)
                                    onDeleteHistoryItem(item)
                                    scope.launch{
                                        val result = snackbarHostState.showSnackbar(
                                            message = "Deleted",
                                            actionLabel = "Undo",
                                            duration = SnackbarDuration.Short
                                        )
                                        if (result == SnackbarResult.ActionPerformed && lastDeleted != null && lastDeletedIndex >= 0) {
                                            onRestoreHistoryItem(lastDeleted!!, lastDeletedIndex)
                                            lastDeleted = null
                                            lastDeletedIndex = -1
                                        }
                                    }
                                },
                                onLongPress = { value ->
                                    onHistoryItemClick(value)
                                }
                            )
                        }
                    }
                }
            }
        }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun HistoryItem(
    item: CalculationHistory,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = NumberButton.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onClick() }
            ) {
                Text(
                    text = item.expression,
                    color = DisplaySecondary,
                    fontSize = 14.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "= ${item.result}",
                    color = DisplayText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Calculation",
                    tint = AccentRed
                )
            }
        }
    }
}

@Composable
private fun SwipeToDeleteHistoryItem(
    item: CalculationHistory,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onLongPress: (String) -> Unit
) {
    val density = LocalDensity.current
    var offsetX by remember { mutableStateOf(0f) }
    val swipeThreshold = with(density) { 100.dp.toPx() }
    val haptic = LocalHapticFeedback.current

    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        label = "offset"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (offsetX > swipeThreshold / 2) AccentRed else Color.Transparent,
        label = "background_color"
    )

    val iconAlpha by animateFloatAsState(
        targetValue = if (offsetX > swipeThreshold / 2) 1f else 0f,
        label = "icon_alpha"
    )

    var showCopyDialog by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
    var showCopySnackbar by remember { mutableStateOf(false) }
    var copiedText by remember { mutableStateOf("") }

    if (showCopyDialog) {
        AlertDialog(
            onDismissRequest = { showCopyDialog = false },
            title = { Text("Copy to Clipboard") },
            text = {
                Column {
                    Text("What do you want to copy?")
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    clipboardManager.setText(AnnotatedString(item.result))
                    copiedText = item.result
                    showCopySnackbar = true
                    showCopyDialog = false
                }) { Text("Result") }
            },
            dismissButton = {
                TextButton(onClick = {
                    clipboardManager.setText(AnnotatedString(item.expression))
                    copiedText = item.expression
                    showCopySnackbar = true
                    showCopyDialog = false
                }) { Text("Expression") }
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(backgroundColor, RoundedCornerShape(12.dp))
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.alpha(iconAlpha)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Delete",
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer(translationX = animatedOffsetX)
                .pointerInput(item) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (offsetX > swipeThreshold) {
                                onDelete()
                            }
                            offsetX = 0f
                        }
                    ) { _, dragAmount ->
                        val newOffset = offsetX + dragAmount
                        offsetX = if (newOffset > 0) newOffset else 0f
                    }
                }
                .pointerInput(item) {
                    detectTapGestures(
                        onLongPress = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            showCopyDialog = true
                        },
                        onTap = { onClick() }
                    )
                },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = NumberButton.copy(alpha = 0.3f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.expression,
                        color = DisplaySecondary,
                        fontSize = 14.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "= ${item.result}",
                        color = DisplayText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Calculation",
                        tint = AccentRed.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }

    if (showCopySnackbar) {
        LaunchedEffect(showCopySnackbar) {
            delay(1200)
            showCopySnackbar = false
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Snackbar(
                modifier = Modifier.align(Alignment.BottomCenter),
                action = {},
                content = { Text("Copied to clipboard") }
            )
        }
    }
}

@Composable
private fun AnimatedSwipeToDeleteHistoryItem(
    item: CalculationHistory,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onLongPress: (String) -> Unit
) {
    var isDeleting by remember { mutableStateOf(false) }
    
    LaunchedEffect(isDeleting) {
        if (isDeleting) {
            delay(300)
            onDelete()
        }
    }
    
    AnimatedVisibility(
        visible = !isDeleting,
        exit = fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(300)
        )
    ) {
        SwipeToDeleteHistoryItem(
            item = item,
            onClick = onClick,
            onDelete = { isDeleting = true },
            onLongPress = onLongPress
        )
    }
}
