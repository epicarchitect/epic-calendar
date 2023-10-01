package epicarchitect.sample

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.material3.Switch as MaterialSwitch

@Composable
fun Button(
    onClick: suspend () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val coroutineScope = rememberCoroutineScope()
    Text(
        modifier = modifier.then(
            Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable(enabled) {
                    coroutineScope.launch {
                        onClick()
                    }
                }
                .padding(8.dp)
        ),
        text = text,
        color = MaterialTheme.colorScheme.onPrimary
    )
}

@Composable
fun Switch(
    onChanged: (Boolean) -> Unit,
    checked: Boolean,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MaterialSwitch(
            onCheckedChange = onChanged,
            checked = checked,
        )

        Text(text = text)
    }
}

@Composable
fun PrevNextButtons(
    onPrev: suspend () -> Unit,
    onNext: suspend () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            text = "prev",
            onClick = {
                coroutineScope.launch {
                    onPrev()
                }
            }
        )

        Button(
            text = "next",
            onClick = {
                coroutineScope.launch {
                    onNext()
                }
            }
        )

        Text(
            text = text
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TestingLayout(
    controls: @Composable ColumnScope.() -> Unit,
    content: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val state = rememberBottomSheetScaffoldState()
    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = state,
        sheetContent = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            coroutineScope.launch {
                                if (state.bottomSheetState.isCollapsed) {
                                    state.bottomSheetState.expand()
                                } else {
                                    state.bottomSheetState.collapse()
                                }
                            }
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.labelLarge
                    )

                    Text(
                        text = getPlatformName(),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    controls()
                }
            }
        },
        sheetPeekHeight = 150.dp,
        sheetElevation = 16.dp
    ) {
        content()
    }
}

@Composable
fun TestingSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.padding(vertical = 4.dp),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    isExpanded = !isExpanded
                }
                .padding(8.dp),
            text = title
        )

        AnimatedVisibility(visible = isExpanded) {
            Column(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                content = content
            )
        }
    }
}