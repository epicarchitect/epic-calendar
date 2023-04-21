package epicarchitect.epic.calendar.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import epicarchitect.epic.calendar.compose.lib.basis.BasisEpicCalendar
import epicarchitect.epic.calendar.compose.lib.datePicker.EpicDatePicker
import epicarchitect.epic.calendar.compose.lib.pager.EpicCalendarPager
import epicarchitect.epic.calendar.compose.ui.theme.EpicCalendarCreationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EpicCalendarCreationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        var currentTesting by remember {
                            mutableStateOf(0)
                        }

                        Row {
                            Button(onClick = { currentTesting -= 1 }) {
                                Text("prev")
                            }
                            Button(onClick = { currentTesting += 1 }) {
                                Text("next")
                            }
                        }


                        when (currentTesting) {
                            0 -> BasisTesting()
                            1 -> PagerTesting()
                            2 -> DatePickerTesting()
                            3 -> {
                                HorizontalPager(
                                    modifier = Modifier.fillMaxSize(),
                                    pageCount = 55
                                ) { page ->
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(100.dp),
                                        text = " alkjd adsjlk asdj askdjklas djajd kd jksad jsjadjsad kdkj fhgjhgjg  fhshg djkfhgjfhghdfuhsfhiuhkjsssjfhajhfhskfjhfsjf sfhs dfhsfh djf gjs fkegskgf dlhfiqwhfoidfhbnvldkv"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BasisTesting() {
    BasisEpicCalendar()
}

@Composable
fun PagerTesting() {
    val state = EpicCalendarPager.rememberState()

    EpicCalendarPager(
        modifier = Modifier
            .border(1.dp, Color.Red)
            .animateContentSize(),
        state = state,
    )

    val scope = rememberCoroutineScope()


    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                scope.launch {
                    state.scrollMonths(-1)
                }
            }
        ) {
            Text("prev month")
        }

        Text(state.currentMonth.month.name)

        Button(
            onClick = {
                scope.launch {
                    state.scrollMonths(1)
                }
            }
        ) {
            Text("next month")
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                scope.launch {
                    state.scrollYears(-1)
                }
            }
        ) {
            Text("prev year")
        }

        Text(state.currentMonth.year.toString())

        Button(
            onClick = {
                scope.launch {
                    state.scrollYears(1)
                }
            }
        ) {
            Text("next year")
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = state.displayDaysOfAdjacentMonths,
            onCheckedChange = {
                state.displayDaysOfAdjacentMonths = it
            }
        )

        Text("displayDaysOfAdjacentMonths")
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = state.displayDaysOfWeek,
            onCheckedChange = {
                state.displayDaysOfWeek = it
            }
        )

        Text("displayDaysOfWeek")
    }
}

@Composable
fun DatePickerTesting() {
    val state = EpicDatePicker.rememberState()

    EpicDatePicker(
        modifier = Modifier
            .border(1.dp, Color.Red)
            .animateContentSize(),
        state = state,
        config = EpicDatePicker.DefaultConfig.copy(
            selectionContentColor = Color.White,
            selectionContainerColor = Color.Red
        )
    )

    val scope = rememberCoroutineScope()


    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                scope.launch {
                    state.pagerState.scrollMonths(-1)
                }
            }
        ) {
            Text("prev month")
        }

        Text(state.pagerState.currentMonth.month.name)

        Button(
            onClick = {
                scope.launch {
                    state.pagerState.scrollMonths(1)
                }
            }
        ) {
            Text("next month")
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                scope.launch {
                    state.pagerState.scrollYears(-1)
                }
            }
        ) {
            Text("prev year")
        }

        Text(state.pagerState.currentMonth.year.toString())

        Button(
            onClick = {
                scope.launch {
                    state.pagerState.scrollYears(1)
                }
            }
        ) {
            Text("next year")
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = state.displayDaysOfAdjacentMonths,
            onCheckedChange = {
                state.displayDaysOfAdjacentMonths = it
            }
        )

        Text("displayDaysOfAdjacentMonths")
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = state.displayDaysOfWeek,
            onCheckedChange = {
                state.displayDaysOfWeek = it
            }
        )

        Text("displayDaysOfWeek")
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = { state.selectionMode = EpicDatePicker.SelectionMode.Single }) {
            Text("Single")
        }

        Button(onClick = { state.selectionMode = EpicDatePicker.SelectionMode.Multi(3) }) {
            Text("Multi(3)")
        }

        Button(onClick = { state.selectionMode = EpicDatePicker.SelectionMode.Range }) {
            Text("Range")
        }
    }
}