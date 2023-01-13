# Fancy Switch

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/dev.sasikanth/fancy-switch/badge.svg)](https://maven-badges.herokuapp.com/maven-central/dev.sasikanth/fancy-switch)

A Material 3 esque button with a fancy reveal animations when toggling between states

![](/art/switch.gif)

## Setup

Add the dependency to your modules `build.gradle` file

```kotlin
dependencies {
  implementation("dev.sasikanth:fancy-switch:<latest-version>")
}
```

## How to use

You can add the component to your Composable like so

```kotlin
@Composable
fun SwitchDemo() {
  val checkedState = remember { mutableStateOf(false) }
  FancySwitch(
    modifier = Modifier.padding(16.dp),
    checked = checkedState
  ) {
    checkedState.value = it
  }
}
```

By default the component uses colors from the `MaterialTheme.colorScheme`. But you can override
the colors using `SwitchDefaults.colors()` function

```kotlin
FancySwitch(
  switchColors = SwitchDefaults.colors(
    uncheckedTrackColor = // your-color-here,
    checkedTrackColor = // your-color-here
  )
) {
  
}
```

`SwitchDefaults.colors` allows configuring the following colors

```kotlin
fun colors(
  uncheckedTrackColor: Color,
  checkedTrackColor: Color,
  disabledTrackColor: Color,
  uncheckedThumbColor: Color,
  checkedThumbColor: Color,
  disabledThumbColor: Color,
  uncheckedIconTint: Color,
  checkedIconTint: Color,
  disabledIconTint: Color,
  rippleColor: Color
)
```

## License

```
Copyright 2023 Sasikanth Miriyampalli

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
