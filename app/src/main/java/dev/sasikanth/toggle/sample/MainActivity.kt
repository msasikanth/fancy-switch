package dev.sasikanth.toggle.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.sasikanth.fancy.toggle.FancySwitch
import dev.sasikanth.toggle.sample.ui.theme.FancySwitchTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      FancySwitchTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          Switch()
        }
      }
    }
  }
}

@Composable
fun Switch() {
  var checked by remember {
    mutableStateOf(false)
  }

  FancySwitch(checked = checked) {
    checked = !checked
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  FancySwitchTheme {
    Switch()
  }
}
