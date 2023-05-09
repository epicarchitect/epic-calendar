# Epic Calendar

Compose Multiplatform library for displaying epic calendars.

![epic-calendar](demo.gif)

### Kotlin Multiplatform supported targets

```Kotlin
kotlin {
    android()
    jvm("desktop")
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    js(IR) { browser() }
}
```

### ATTENTION

This library is super experimental!

### Dependencies

The library is published on MavenCentral.

```Kotlin
dependencies {
    commonMainImplementation("io.github.epicarchitect:calendar-compose-basis:1.0.4")
    commonMainImplementation("io.github.epicarchitect:calendar-compose-ranges:1.0.4") // includes basis
    commonMainImplementation("io.github.epicarchitect:calendar-compose-pager:1.0.4") // includes basis
    commonMainImplementation("io.github.epicarchitect:calendar-compose-datepicker:1.0.4") // includes pager + ranges
}
```

### Basis calendar setup

```kotlin
BasisEpicCalendar(
    state = rememberBasisEpicCalendarState(
        config = rememberBasisEpicCalendarConfig(
            rowsSpacerHeight = 4.dp,
            dayOfWeekViewHeight = 40.dp,
            dayOfMonthViewHeight = 40.dp,
            columnWidth = 40.dp,
            dayOfWeekShape = RoundedCornerShape(16.dp),
            dayOfMonthShape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(0.dp),
            contentColor = Color.Unspecified,
            displayDaysOfAdjacentMonths = true,
            displayDaysOfWeek = true
        )
    )
)
```

If you want to change config dynamically, use `rememberMutableBasisEpicCalendarConfig()`.
If you want to change state dynamically, use `rememberMutableBasisEpicCalendarState()`.

### Pager calendar setup

```kotlin
EpicCalendarPager(
    state = rememberEpicCalendarPagerState(
        config = rememberEpicCalendarPagerConfig(
            basisConfig = rememberBasisEpicCalendarConfig(
                contentColor = Color.Red
            )
        )
    )
)
```

The `EpicCalendarPager` is based on a `BasisEpicCalendar` so it accepts a `basisConfig`,
you can pass it using `rememberEpicCalendarPagerConfig`.
So you can customize the appearance using `rememberBasisEpicCalendarConfig`.

### Date picker setup

```kotlin
EpicDatePicker(
    state = rememberEpicDatePickerState(
        config = rememberEpicDatePickerConfig(
            pagerConfig = rememberEpicCalendarPagerConfig(
                basisConfig = rememberBasisEpicCalendarConfig(
                    displayDaysOfAdjacentMonths = false
                )
            ),
            selectionContentColor = MaterialTheme.colorScheme.onPrimary,
            selectionContainerColor = MaterialTheme.colorScheme.primary
        )
    )
)
```

The `EpicDatePicker` is based on a `EpicCalendarPager` so it accepts a `pagerConfig`,
you can pass it using `rememberEpicDatePickerConfig`.

### Date picker selection mode

Date picker has 3 features to pick dates:

- Single date selection
- Multiple date selection
- Date range selection

```kotlin
EpicDatePicker(
    state = rememberEpicDatePickerState(
        // for range
        selectionMode = EpicDatePickerState.SelectionMode.Range,
        // or for single date
        selectionMode = EpicDatePickerState.SelectionMode.Single(),
        // or for multiselect
        selectionMode = EpicDatePickerState.SelectionMode.Single(maxSize = 5)
    )
)
```

Selected dates can be obtained from `state.selectedDates`.

### Draw ranges

To draw ranges use `Modifier.drawEpicRanges(ranges, color)`
from `io.github.epicarchitect:calendar-compose-ranges`.

```Kotlin
val myRanges: List<ClosedRange<kotlinx.datetime.LocalDate>>
val myRangeColor: androidx.compose.ui.graphics.Color

// for simple BasisEpicCalendar
BasisEpicCalendar(
    modifier = Modifier.drawEpicRanges(
        ranges = myRanges,
        color = myRangeColor
    )
)

// and for pager
EpicCalendarPager(
    pageModifier = { page ->
        Modifier.drawEpicRanges(
            ranges = myRanges,
            color = myRangeColor
        )
    }
)
```

### Provide setup globally

```kotlin
val defaultBasisConfig = DefaultBasisEpicCalendarConfig.copy(
    contentPadding = PaddingValues(horizontal = 16.dp),
    displayDaysOfAdjacentMonths = false,
    displayDaysOfWeek = false,
    dayOfMonthShape = RoundedCornerShape(8.dp),
    rowsSpacerHeight = 2.dp
)
val defaultPagerConfig = DefaultEpicCalendarPagerConfig.copy(
    basisConfig = defaultBasisConfig
)
val defaultDatePickerConfig = DefaultEpicDatePickerConfig.copy(
    pagerConfig = defaultPagerConfig,
    selectionContainerColor = MaterialTheme.colorScheme.primary,
    selectionContentColor = MaterialTheme.colorScheme.onPrimary
)
CompositionLocalProvider(
    LocalBasisEpicCalendarConfig provides defaultBasisConfig,
    LocalEpicCalendarPagerConfig provides defaultPagerConfig,
    LocalEpicDatePickerConfig provides defaultDatePickerConfig
) {
    YourApplicationThatUsesEpicCalendars()
}
```

### Samples

More examples can be found in the [sample directory](sample).

### License

```
MIT License

Copyright (c) 2023 Alexander Kolmachikhin

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
