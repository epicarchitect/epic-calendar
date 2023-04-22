# Epic Calendar
Jetpack Compose library for displaying epic calendars.
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
    implementation("io.github.epicarchitect:calendar-compose-basis:1.0.0")
    implementation("io.github.epicarchitect:calendar-compose-pager:1.0.0") // includes basis
    implementation("io.github.epicarchitect:calendar-compose-date-picker:1.0.0") // includes pager
}
```
