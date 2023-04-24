# Epic Calendar

Jetpack Compose library for displaying epic calendars.

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
    implementation("io.github.epicarchitect:calendar-compose-basis:1.0.2")
    implementation("io.github.epicarchitect:calendar-compose-ranges:1.0.2") // includes basis
    implementation("io.github.epicarchitect:calendar-compose-pager:1.0.2") // includes basis
    implementation("io.github.epicarchitect:calendar-compose-date-picker:1.0.2") // includes pager + ranges
}
```

### Draw ranges

To draw ranges use `Modifier.drawEpicRanges(ranges, color)`

for simple BasisEpicCalendar:

```Kotlin
val myRanges: List<ClosedRange<LocalDate>>
val myRangeColor: Color
BasisEpicCalendar(
    modifier = Modifier.drawEpicRanges(
        ranges = myRanges,
        color = myRangeColor
    ),
)
```

and for pager:

```Kotlin
val myRanges: List<ClosedRange<LocalDate>>
val myRangeColor: Color
EpicCalendarPager(
    pageModifier = {
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