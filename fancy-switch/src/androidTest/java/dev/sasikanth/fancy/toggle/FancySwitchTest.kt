package dev.sasikanth.fancy.toggle

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

class FancySwitchTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun switch_should_work_correctly() {
    // given
    var checked by mutableStateOf(false)

    composeTestRule.setContent {
      MaterialTheme {
        FancySwitch(
          checked = checked,
          onValueChange = {
            checked = it
          }
        )
      }
    }

    assertThat(checked).isEqualTo(false)

    // when
    composeTestRule
      .onRoot()
      .performClick()

    // then
    assertThat(checked).isEqualTo(true)
  }

  @Test
  fun disabling_switch_should_work_correctly() {
    // given
    composeTestRule.setContent {
      MaterialTheme {
        FancySwitch(
          checked = false,
          enabled = false,
          onValueChange = {
            // Handle value changes
          }
        )
      }
    }

    // then
    composeTestRule
      .onNodeWithTag("Switch")
      .assertIsNotEnabled()
  }

  @Test
  fun enabling_switch_should_work_correctly() {
    // given
    composeTestRule.setContent {
      MaterialTheme {
        FancySwitch(
          checked = false,
          enabled = true,
          onValueChange = {
            // Handle value changes
          }
        )
      }
    }

    // then
    composeTestRule
      .onNodeWithTag("Switch")
      .assertIsEnabled()
      .assertHasClickAction()
  }
}
