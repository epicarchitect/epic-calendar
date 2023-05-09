package epicarchitect.sample

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.material3.Button as MaterialButton
import androidx.compose.material3.Switch as MaterialSwitch

@Composable
fun Button(
    onClick: suspend () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val coroutineScope = rememberCoroutineScope()
    MaterialButton(
        modifier = modifier,
        onClick = { coroutineScope.launch { onClick() } },
        enabled = enabled
    ) {
        Text(text)
    }
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
        horizontalArrangement = Arrangement.spacedBy(16.dp),
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
        scaffoldState = state,
        sheetContent = {
            Column {
                Text(
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
                        .padding(16.dp),
                    text = "Settings",
                    style = MaterialTheme.typography.headlineSmall
                )
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    controls()
                }
            }
        },
        sheetShape = RoundedCornerShape(16.dp),
        sheetPeekHeight = 250.dp,
        sheetElevation = 16.dp
    ) {
        content()
    }
}