# Epic Calendar

Compose Multiplatform library for displaying epic calendars.

Support: android, iOS, macos, desktop, web.

### Demo

![epic-calendar](.github/demo.gif)

### ATTENTION

This library is super experimental!

### Features

- Single date selection
- Multiple date selection
- Date range selection

### Add the MavenCentral repository

```Kotlin
repositories {
    mavenCentral()
}
```

### Add dependencies

```Kotlin
dependencies {
    implementation("io.github.epicarchitect:calendar-compose-basis:1.0.3")
    implementation("io.github.epicarchitect:calendar-compose-ranges:1.0.3") // includes basis
    implementation("io.github.epicarchitect:calendar-compose-pager:1.0.3") // includes basis
    implementation("io.github.epicarchitect:calendar-compose-datepicker:1.0.3") // includes pager + ranges
}
```

### Draw ranges

To draw ranges use `Modifier.drawEpicRanges(ranges, color)`

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
