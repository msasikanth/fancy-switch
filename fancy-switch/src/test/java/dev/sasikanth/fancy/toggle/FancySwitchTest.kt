package dev.sasikanth.fancy.toggle

import androidx.compose.material3.MaterialTheme
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

class FancySwitchTest {

  @get:Rule
  val paparazzi = Paparazzi(
    deviceConfig = DeviceConfig.NEXUS_5,
    showSystemUi = false,
    renderingMode = SHRINK,
    theme = "android:Theme.Material.Light.NoActionBar"
  )

  @Test
  fun `switch turned on`() {
    paparazzi.snapshot {
      MaterialTheme {
        FancySwitch(checked = true)
      }
    }
  }

  @Test
  fun `switch turned off`() {
    paparazzi.snapshot {
      MaterialTheme {
        FancySwitch(checked = false)
      }
    }
  }

  @Test
  fun `switch turned on and disabled`() {
    paparazzi.snapshot {
      MaterialTheme {
        FancySwitch(checked = true, enabled = false)
      }
    }
  }

  @Test
  fun `switch turned off and disabled`() {
    paparazzi.snapshot {
      MaterialTheme {
        FancySwitch(checked = false, enabled = false)
      }
    }
  }
}
