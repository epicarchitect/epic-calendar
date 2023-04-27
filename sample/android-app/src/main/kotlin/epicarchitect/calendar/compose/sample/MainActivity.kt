package epicarchitect.calendar.compose.sample

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import java.time.Month

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

//
//@Composable
//fun BasisTesting() {
//    val basisState = BasisEpicCalendar.rememberState()
//    val testMonth = basisState.currentMonth
//    val rangeColor = MaterialTheme.colorScheme.primaryContainer
//    val ranges = remember(testMonth, rangeColor) {
//        listOf(
//            testMonth
//                .atStartDay()
//                .let { it..it },
//            testMonth
//                .atStartDay()
//                .let {
//                    it.plus(1, DateTimeUnit.DAY)..it.plus(3, DateTimeUnit.DAY)
//                }
//        )
//    }
//
//    BasisEpicCalendar(
////        modifier = Modifier.drawEpicRanges(
////            ranges = ranges,
////            color = rangeColor
////        ),
//        state = basisState
//    )
//}
//
//@Composable
//fun PagerTesting() {
//    val state = EpicCalendarPager.rememberState()
//    val testMonth = state.monthRange.start.next()
//    val rangeColor1 = MaterialTheme.colorScheme.primaryContainer
//    val rangeColor2 = Color.Red.copy(alpha = 0.4f)
//    val ranges1 = remember(testMonth) {
//        listOf(
//            testMonth
//                .atStartDay()
//                .let { it..it },
//            testMonth
//                .atStartDay()
//                .let {
//                    it.plus(1, DateTimeUnit.DAY)..it.plus(3, DateTimeUnit.DAY)
//                }
//        )
//    }
//
//    val ranges2 = remember(testMonth) {
//        listOf(
//            testMonth
//                .atStartDay()
//                .let {
//                    it.plus(2, DateTimeUnit.DAY)..it.plus(5, DateTimeUnit.DAY)
//                },
//            testMonth.next()
//                .atStartDay()
//                .let {
//                    it.plus(1, DateTimeUnit.DAY)..it.plus(3, DateTimeUnit.DAY)
//                }
//        )
//    }
//
//    EpicCalendarPager(
//        pageModifier = {
//            Modifier
//                .drawEpicRanges(
//                    ranges = ranges1,
//                    color = rangeColor1
//                )
//                .drawEpicRanges(
//                    ranges = ranges2,
//                    color = rangeColor2
//                )
//        },
//        state = state
//    )
//
//    val scope = rememberCoroutineScope()
//
//
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Button(
//            onClick = {
//                scope.launch {
//                    state.scrollMonths(-1)
//                }
//            }
//        ) {
//            Text("prev month")
//        }
//
//        Text(state.currentMonth.month.name)
//
//        Button(
//            onClick = {
//                scope.launch {
//                    state.scrollMonths(1)
//                }
//            }
//        ) {
//            Text("next month")
//        }
//    }
//
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Button(
//            onClick = {
//                scope.launch {
//                    state.scrollYears(-1)
//                }
//            }
//        ) {
//            Text("prev year")
//        }
//
//        Text(state.currentMonth.year.toString())
//
//        Button(
//            onClick = {
//                scope.launch {
//                    state.scrollYears(1)
//                }
//            }
//        ) {
//            Text("next year")
//        }
//    }
//
//    Row(
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Checkbox(
//            checked = state.displayDaysOfAdjacentMonths,
//            onCheckedChange = {
//                state.displayDaysOfAdjacentMonths = it
//            }
//        )
//
//        Text("displayDaysOfAdjacentMonths")
//    }
//
//    Row(
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Checkbox(
//            checked = state.displayDaysOfWeek,
//            onCheckedChange = {
//                state.displayDaysOfWeek = it
//            }
//        )
//
//        Text("displayDaysOfWeek")
//    }
//}
//
//@Composable
//fun DatePickerTesting() {
//    val state = EpicDatePicker.rememberState()
//
//    EpicDatePicker(state = state)
//
//    val scope = rememberCoroutineScope()
//
//
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Button(
//            onClick = {
//                scope.launch {
//                    state.pagerState.scrollMonths(-1)
//                }
//            }
//        ) {
//            Text("prev month")
//        }
//
//        Text(state.pagerState.currentMonth.month.name)
//
//        Button(
//            onClick = {
//                scope.launch {
//                    state.pagerState.scrollMonths(1)
//                }
//            }
//        ) {
//            Text("next month")
//        }
//    }
//
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Button(
//            onClick = {
//                scope.launch {
//                    state.pagerState.scrollYears(-1)
//                }
//            }
//        ) {
//            Text("prev year")
//        }
//
//        Text(state.pagerState.currentMonth.year.toString())
//
//        Button(
//            onClick = {
//                scope.launch {
//                    state.pagerState.scrollYears(1)
//                }
//            }
//        ) {
//            Text("next year")
//        }
//    }
//
//    Row(
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Checkbox(
//            checked = state.displayDaysOfAdjacentMonths,
//            onCheckedChange = {
//                state.displayDaysOfAdjacentMonths = it
//            }
//        )
//
//        Text("displayDaysOfAdjacentMonths")
//    }
//
//    Row(
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Checkbox(
//            checked = state.displayDaysOfWeek,
//            onCheckedChange = {
//                state.displayDaysOfWeek = it
//            }
//        )
//
//        Text("displayDaysOfWeek")
//    }
//
//    Row(
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Button(onClick = { state.selectionMode = EpicDatePicker.SelectionMode.Single() }) {
//            Text("Single")
//        }
//
//        Button(onClick = { state.selectionMode = EpicDatePicker.SelectionMode.Single(3) }) {
//            Text("Single(3)")
//        }
//
//        Button(onClick = { state.selectionMode = EpicDatePicker.SelectionMode.Range }) {
//            Text("Range")
//        }
//    }
//}